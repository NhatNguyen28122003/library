package com.nguyenvannhat.library.services.books;

import com.nguyenvannhat.library.constant.Constant;
import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.exceptions.DataNotFoundException;
import com.nguyenvannhat.library.exceptions.InvalidDataException;
import com.nguyenvannhat.library.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public void insertBook(BookDTO bookDTO) throws InvalidDataException {
        try {
            Book book = bookRepository.findByTitle(bookDTO.getTitle()).orElse(null);
            if (book != null) {
                book.setQuantity(book.getQuantity() + 1);
                bookRepository.save(book);
            } else {
                book = Book.builder()
                        .title(bookDTO.getTitle())
                        .author(bookDTO.getAuthor())
                        .pages(bookDTO.getPages())
                        .build();
                bookRepository.save(book);
            }
        } catch (Exception e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public void insertBooks(MultipartFile multipartFile) throws InvalidDataException {
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
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public void updateBook(Long id, BookDTO bookDTO) throws DataNotFoundException {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException(Constant.BOOK_NOT_FOUND)
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
    public void deleteBook(BookDTO bookDTO) throws DataNotFoundException {
        Book book = bookRepository.findByTitle(bookDTO.getTitle()).orElseThrow(
                () -> new DataNotFoundException("Book not found")
        );
        bookRepository.deleteById(book.getId());

    }

    @Override
    public void deleteBookByID(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public File exportBooksToExcel(List<BookDTO> books) throws DataNotFoundException, FileNotFoundException {
        if (books == null || books.isEmpty()) {
            throw new DataNotFoundException("No books available to export.");
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
