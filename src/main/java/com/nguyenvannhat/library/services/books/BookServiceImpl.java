package com.nguyenvannhat.library.services.books;

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

import java.io.FileOutputStream;
import java.io.InputStream;
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
    public Book insertBook(BookDTO bookDTO) throws Exception {
        try {
            Book book = bookRepository.findByTitle(bookDTO.getTitle());
            if (book != null) {
                book.setQuantity(book.getQuantity() + 1);
                return bookRepository.save(book);
            } else {
                book = Book.builder()
                        .title(bookDTO.getTitle())
                        .author(bookDTO.getAuthor())
                        .pages(bookDTO.getPages())
                        .build();
                return bookRepository.save(book);
            }
        } catch (Exception e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public List<Book> insertBooks(MultipartFile multipartFile) throws Exception {
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
            return bookRepository.findAll();
        } catch (Exception e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public Book updateBook(Long id, BookDTO bookDTO) throws Exception {
        try {
            Book book = bookRepository.findById(id).get();
            if (!bookDTO.getTitle().isEmpty()) {
                book.setTitle(bookDTO.getTitle());
            }
            if (!bookDTO.getAuthor().isEmpty()) {
                book.setAuthor(bookDTO.getAuthor());
            }
            if (bookDTO.getPages() > 0) {
                book.setPages(bookDTO.getPages());
            }
            return bookRepository.save(book);
        } catch (Exception e) {
            throw new DataNotFoundException("Book not found!");
        }
    }

    @Override
    public void deleteBook(BookDTO bookDTO) throws Exception {
        try {
            Book book = bookRepository.findByTitle(bookDTO.getTitle());
            bookRepository.deleteById(book.getId());
        } catch (Exception e) {
            throw new DataNotFoundException("Book not found!");
        }
    }

    @Override
    public void deleteBookByID(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void exportBooksToExcel(List<BookDTO> books) throws Exception {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Books");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Title", "Author", "Pages"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            int rowIndex = 1;
            for (BookDTO book : books) {
                Row dataRow = sheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(book.getTitle());
                dataRow.createCell(1).setCellValue(book.getAuthor());
                dataRow.createCell(2).setCellValue(book.getPages());
            }
            String filePath = "books.xlsx";
            workbook.write(new FileOutputStream(filePath));
            workbook.close();
        } catch (Exception e) {
            throw new DataNotFoundException("Books not found!");
        }
    }
}
