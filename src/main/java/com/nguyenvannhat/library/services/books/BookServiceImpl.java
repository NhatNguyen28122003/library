package com.nguyenvannhat.library.services.books;

import com.nguyenvannhat.library.constants.Constant;
import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.dtos.requests.BookRequest;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.BookCategories;
import com.nguyenvannhat.library.entities.Category;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.repositories.BookCategoryRepository;
import com.nguyenvannhat.library.repositories.BookRepository;
import com.nguyenvannhat.library.repositories.CategoriesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final CategoriesRepository categoriesRepository;
    private final ModelMapper modelMapper;

    @Override
    public BookDTO addBook(BookRequest book) {
        Optional<Book> existingBook = bookRepository.findByCode(book.getCode());
        if (existingBook.isPresent()) {
            log.error(String.format("Book with code %s already exists", book.getCode()));
            throw new ApplicationException(Constant.ERROR_BOOK_NOT_FOUND);
        }

        Book newBook = Book.builder().code(book.getCode()).title(book.getTitle()).author(book.getAuthor()).url(book.getUrl()).pages(book.getPages()).build();
        newBook = bookRepository.save(newBook);
        if (book.getCategoryCode() != null) {
            addBookToCategory(newBook, book.getCategoryCode());
        }
        return modelMapper.map(newBook, BookDTO.class);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(book -> modelMapper.map(book, BookDTO.class)).collect(Collectors.toList());
    }

    @Override
    public BookDTO update(Long id, BookRequest book) {
        Book currentBook = bookRepository.findById(id).orElseThrow(() -> {
            log.error(String.format("Book with id %s not found", id));
            return new ApplicationException(Constant.ERROR_BOOK_NOT_FOUND);
        });

        Optional<Book> existingBook = bookRepository.findByCode(book.getCode());
        if (existingBook.isEmpty()) {
            currentBook.setCode(book.getCode());
        }

        if (book.getTitle() != null) {
            currentBook.setTitle(book.getTitle());
        }

        if (book.getAuthor() != null) {
            currentBook.setAuthor(book.getAuthor());
        }

        if (book.getUrl() != null) {
            currentBook.setUrl(book.getUrl());
        }
        currentBook = bookRepository.save(currentBook);
        return modelMapper.map(currentBook, BookDTO.class);
    }

    @Override
    public void delete(Long id) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            Book currentBook = existingBook.get();
            currentBook.setIsDeleted(true);
            bookRepository.save(currentBook);
        }
    }

    @Override
    public BookDTO getBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_BOOK_NOT_FOUND)
        );
        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    @Transactional
    public List<BookDTO> importExcel(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet("Books");
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Skip header

            List<Book> currentAllBooks = bookRepository.findAll();
            Set<String> currentBookCodes = currentAllBooks.stream().map(Book::getCode).collect(Collectors.toSet());

            List<Book> newBooks = new ArrayList<>();
            Map<String, List<String>> bookToCategoryCodes = new HashMap<>();
            Set<String> allCategoryCodesInFile = new HashSet<>();

            // 1. Đọc file và gom dữ liệu
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String code = row.getCell(0).getStringCellValue();
                if (Objects.isNull(code) || currentBookCodes.contains(code)) continue;

                String title = row.getCell(1).getStringCellValue();
                String author = row.getCell(2).getStringCellValue();
                String url = row.getCell(3).getStringCellValue();
                Integer pages = Integer.parseInt(row.getCell(4).getStringCellValue());

                Book book = Book.builder().code(code).title(title).author(author).url(url).pages(pages).build();

                newBooks.add(book);

                if (row.getCell(5) != null) {
                    List<String> categoryCodes = Arrays.stream(row.getCell(5).getStringCellValue().split(", ")).toList();
                    bookToCategoryCodes.put(code, categoryCodes);
                    allCategoryCodesInFile.addAll(categoryCodes);
                }
            }

            // 2. Truy vấn toàn bộ category 1 lần duy nhất
            List<Category> allCategories = categoriesRepository.findByCodeIn(new ArrayList<>(allCategoryCodesInFile));
            Map<String, Long> categoryCodeToId = allCategories.stream().collect(Collectors.toMap(Category::getCode, Category::getId));

            // 3. Lưu toàn bộ sách (phải lưu trước để có ID)
            List<Book> savedBooks = bookRepository.saveAll(newBooks);
            Map<String, Long> bookCodeToId = savedBooks.stream().collect(Collectors.toMap(Book::getCode, Book::getId));

            // 4. Tạo danh sách BookCategories một lần
            List<BookCategories> bookCategories = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : bookToCategoryCodes.entrySet()) {
                String bookCode = entry.getKey();
                Long bookId = bookCodeToId.get(bookCode);
                for (String catCode : entry.getValue()) {
                    Long categoryId = categoryCodeToId.get(catCode);
                    if (categoryId != null) {
                        bookCategories.add(BookCategories.builder().bookId(bookId).categoryId(categoryId).build());
                    }
                }
            }

            bookCategoryRepository.saveAll(bookCategories);
            return savedBooks.stream().map(book -> modelMapper.map(book, BookDTO.class)).collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Error during importExcel: {}", e.getMessage(), e);
            throw new ApplicationException(Constant.ERROR_BOOK_NOT_FOUND);
        }
    }


    @Override
    public byte[] exportExcel() {
        return new byte[0];
    }

    private void addBookToCategory(Book book, List<String> categoryCode) {
        List<Category> categories = categoriesRepository.findByCodeIn(categoryCode);
        List<Long> categoriesIds = categories.stream().map(Category::getId).toList();
        List<BookCategories> bookCategories = new ArrayList<>();
        for (Long categoryId : categoriesIds) {
            bookCategories.add(BookCategories.builder().bookId(book.getId()).categoryId(categoryId).build());
        }
        bookCategoryRepository.saveAll(bookCategories);
    }
}
