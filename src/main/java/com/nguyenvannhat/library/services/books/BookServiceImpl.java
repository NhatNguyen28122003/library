package com.nguyenvannhat.library.services.books;

import com.nguyenvannhat.library.constants.Constant;
import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.BookCategories;
import com.nguyenvannhat.library.repositories.BookCateRepository;
import com.nguyenvannhat.library.repositories.BookRepository;
import com.nguyenvannhat.library.repositories.CategoriesRepository;
import com.nguyenvannhat.library.responses.CustomResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookCateRepository bookCateRepository;
    private final CategoriesRepository categoriesRepository;
    private final MessageSource messageSource;
    private static final Locale locale = Locale.ENGLISH;
    private final ModelMapper modelMapper;

    @Override
    public CustomResponse<List<Book>> addBook(BookDTO bookDTO) {
        Book book = Book.builder().title(bookDTO.getTitle()).author(bookDTO.getAuthor()).pages(bookDTO.getPages()).build();
        Long bookId = bookRepository.save(book).getId();
        List<BookCategories> bookCategories = new ArrayList<>();
        for (String category : bookDTO.getCategoryName()) {
            if (categoriesRepository.findByName(category).isEmpty()) {
                continue;
            }
            Long categoryId = categoriesRepository.findByName(category).get().getId();
            bookCategories.add(BookCategories.builder().bookId(bookId).categoryId(categoryId).build());
        }
        bookCateRepository.saveAll(bookCategories);
        return findAllBook();
    }

    @Override
    @Transactional
    public CustomResponse<List<Book>> addMultiBook(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next();
        Row row;
        List<BookDTO> bookDTOS = new ArrayList<>();
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            List<String> categories = Arrays.asList(row.getCell(3).getStringCellValue().split(","));
            bookDTOS.add(BookDTO.builder()
                    .title(row.getCell(0).getStringCellValue())
                    .author(row.getCell(1).getStringCellValue())
                    .pages((int) row.getCell(2).getNumericCellValue())
                    .categoryName(categories)
                    .build());
        }
        workbook.close();
        List<Book> books = new ArrayList<>();
        for (BookDTO bookDTO : bookDTOS) {
            books.add(modelMapper.map(bookDTO, Book.class));
        }
        List<Book> saveBooks = bookRepository.saveAll(books);
        List<BookCategories> bookCategories = new ArrayList<>();
        for (int i = 0; i < bookDTOS.size(); i++) {
            for (String category : bookDTOS.get(i).getCategoryName()) {
                if (categoriesRepository.findByName(category).isPresent()) {
                    Long categoryId = categoriesRepository.findByName(category).get().getId();
                    bookCategories.add(BookCategories.builder()
                            .bookId(saveBooks.get(i).getId())
                            .categoryId(categoryId)
                            .build());
                }
            }
        }
        bookCateRepository.saveAll(bookCategories);
        return findAllBook();
    }


    @Override
    public CustomResponse<List<Book>> findAllBook() {
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_CATEGORY_GET_INFORMATION, null, locale),
                bookRepository.findAll());
    }

    @Override
    public CustomResponse<List<Book>> findBookByTitle(String title) {
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_CATEGORY_GET_INFORMATION, null, locale),
                bookRepository.findBookByTitle(title));
    }

    @Override
    public CustomResponse<File> exportBooksToExcel() throws IOException {
        List<Book> books = bookRepository.findAll();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Books");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Id");
            headerRow.createCell(1).setCellValue("Title");
            headerRow.createCell(2).setCellValue("Author");
            headerRow.createCell(3).setCellValue("Pages");
            int rowNum = 1;
            for (Book book : books) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(book.getId());
                row.createCell(1).setCellValue(book.getTitle());
                row.createCell(2).setCellValue(book.getAuthor());
                row.createCell(3).setCellValue(book.getPages());
            }
            OutputStream outputStream = new FileOutputStream("book.xlsx");
            workbook.write(outputStream);
            outputStream.close();
            return CustomResponse.download(HttpStatus.ACCEPTED.value(),
                    messageSource.getMessage(Constant.SUCCESS_BOOK_GET_INFORMATION, null, locale),
                    new File("book.xlsx"));
        }
    }

    @Override
    public CustomResponse<List<Book>> deleteBookByTitle(String title) {
        List<Book> books = bookRepository.findBookByTitle(title);
        bookRepository.deleteAll(books);
        return findAllBook();
    }
}
