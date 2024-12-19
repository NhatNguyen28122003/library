package com.nguyenvannhat.library.services.books;


import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.BookCategories;
import com.nguyenvannhat.library.entities.Category;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.exceptions.ErrorCode;
import com.nguyenvannhat.library.repositories.BookCategoryRepository;
import com.nguyenvannhat.library.repositories.BookRepository;
import com.nguyenvannhat.library.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public void insertBook(BookDTO bookDTO) {
        Optional<Book> optionalBook = bookRepository.findByTitle(bookDTO.getTitle());
        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();
            existingBook.setQuantity(existingBook.getQuantity() + 1);
            bookRepository.save(existingBook);
        } else {
            Book book = Book.builder().title(bookDTO.getTitle()).author(bookDTO.getAuthor()).pages(bookDTO.getPages()).build();
            Book book1 = bookRepository.save(book);
            for (String categoryName : bookDTO.getCategoryName()) {
                Category category = categoryRepository.findByName(categoryName).orElseThrow();
                BookCategories bookCategories = BookCategories.builder().bookId(book1.getId()).categoryId(category.getId()).build();
                bookCategoryRepository.save(bookCategories);
            }
        }
    }

    @Override
    public void insertBooks(MultipartFile multipartFile)  {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            Row row;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                BookDTO bookDTO = new BookDTO();
                bookDTO.setTitle(row.getCell(1).getStringCellValue());
                bookDTO.setAuthor(row.getCell(2).getStringCellValue());
                bookDTO.setPages((int) row.getCell(3).getNumericCellValue());
                insertBook(bookDTO);
            }
            bookRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BookDTO> findAllBooksByCategory(Category category) {
        return categoryRepository.findBooksByCategory(category)
                .stream().map(
                        book -> new BookDTO(book.getTitle(),
                                book.getAuthor(),
                                book.getPages(),
                                new ArrayList<>())
                ).collect(Collectors.toList());
    }

    @Override
    public BookDTO getBookById(Long id)  {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.BOOK_NOT_FOUND)
        );
        return new BookDTO(book.getTitle(), book.getAuthor(), book.getPages());
    }

    @Override
    public void updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.BOOK_NOT_FOUND)
        );
        if (!bookDTO.getTitle().isEmpty()) {
            book.setTitle(bookDTO.getTitle());
        }
        if (!bookDTO.getAuthor().isEmpty()) {
            book.setAuthor(bookDTO.getAuthor());
        }
        if (bookDTO.getPages() > 0) {
            book.setPages(bookDTO.getPages());
        }
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteBook(BookDTO bookDTO) {
        Book book = bookRepository.findByTitle(bookDTO.getTitle()).orElseThrow(
                () -> new ApplicationException(ErrorCode.BOOK_NOT_FOUND));
        bookCategoryRepository.deleteByBookId(book.getId());
        bookRepository.deleteById(book.getId());

    }

    @Override
    public void deleteBookByID(Long id) {
        bookCategoryRepository.deleteByBookId(id);
        bookRepository.deleteById(id);
    }

    @Override
    public File exportBooksToExcel(List<BookDTO> books)  {
        if (books == null || books.isEmpty()) {
            throw new ApplicationException(ErrorCode.BOOK_NOT_FOUND);
        }
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Books");
        String[] headers = {"Title", "Author", "Pages"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        int rowIndex = 1;
        for (BookDTO book : books) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(book.getTitle());
            row.createCell(1).setCellValue(book.getAuthor());
            row.createCell(2).setCellValue(book.getPages());
        }

        String filePath = "books.xlsx";
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new File(filePath);
    }

}
