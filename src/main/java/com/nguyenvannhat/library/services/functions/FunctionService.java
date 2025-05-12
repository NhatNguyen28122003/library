package com.nguyenvannhat.library.services.functions;

import com.nguyenvannhat.library.dtos.FunctionDTO;
import com.nguyenvannhat.library.dtos.requests.FunctionRequest;
import com.nguyenvannhat.library.entities.Function;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FunctionService {
    FunctionDTO add(FunctionRequest function);
    FunctionDTO update(Long id, FunctionRequest function);
    List<FunctionDTO> getAllFunction();
    FunctionDTO getFunction(Long id);
    List<FunctionDTO> search(String keyword);
    List<FunctionDTO> importExcel(MultipartFile file);
    byte[] exportExcel();
    void softDeleted(Long id);
    void deleteFunction(Function function);
}
