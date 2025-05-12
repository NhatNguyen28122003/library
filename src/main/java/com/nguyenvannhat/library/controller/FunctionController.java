package com.nguyenvannhat.library.controller;

import com.nguyenvannhat.library.dtos.FunctionDTO;
import com.nguyenvannhat.library.dtos.requests.FunctionRequest;
import com.nguyenvannhat.library.services.functions.FunctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/function")
public class FunctionController {
    private final FunctionService functionService;

    @PostMapping("/create")
    public ResponseEntity<FunctionDTO> addFunction(@RequestBody FunctionRequest function) {
        FunctionDTO functionDTO = functionService.add(function);
        return ResponseEntity.ok(functionDTO);
    }


    @GetMapping("/getAllFunction")
    public ResponseEntity<List<FunctionDTO>> getAllFunction() {
        return ResponseEntity.ok(functionService.getAllFunction());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FunctionDTO> updateFunction(@Param("id") Long id, @RequestBody FunctionRequest function) {
        return ResponseEntity.ok(functionService.update(id, function));
    }

    @PostMapping("/importFile")
    public ResponseEntity<List<FunctionDTO>> importFile(@RequestParam("file") MultipartFile file) {
        List<FunctionDTO> functionDTOS = functionService.importExcel(file);
        return ResponseEntity.ok(functionDTOS);
    }

    @GetMapping("/exportExcel")
    public ResponseEntity<byte[]> exportExcel() {
        byte[] bytes = functionService.exportExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "functions.xlsx");
        return ResponseEntity.ok().headers(headers).body(bytes);
    }
}
