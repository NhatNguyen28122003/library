package com.nguyenvannhat.library.services.functions;

import com.nguyenvannhat.library.constants.Constant;
import com.nguyenvannhat.library.dtos.FunctionDTO;
import com.nguyenvannhat.library.dtos.requests.FunctionRequest;
import com.nguyenvannhat.library.entities.Function;
import com.nguyenvannhat.library.entities.Role;
import com.nguyenvannhat.library.entities.RoleFunction;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.repositories.FunctionRepository;
import com.nguyenvannhat.library.repositories.RoleFunctionRepository;
import com.nguyenvannhat.library.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FunctionServiceImpl implements FunctionService {

    private final FunctionRepository functionRepository;
    private final RoleRepository roleRepository;
    private final RoleFunctionRepository roleFunctionRepository;
    private final ModelMapper modelMapper;

    @Override
    public FunctionDTO add(FunctionRequest functionRequest) {
        Optional<Function> existingFunction = functionRepository.findByCode(functionRequest.getCode().toLowerCase());
        if (existingFunction.isPresent()) {
            log.error("Function with code {} already exists", functionRequest.getCode());
            throw new ApplicationException(Constant.ERROR_FUNCTION_CODE_EXISTS);
        }

        Function newFunction = Function.builder()
                .code(functionRequest.getCode().toLowerCase())
                .name(functionRequest.getName())
                .description(functionRequest.getDescription())
                .build();
        newFunction = functionRepository.save(newFunction);
        Optional<Role> existingAdminRole = roleRepository.findByCode("admin");
        if (existingAdminRole.isPresent()) {
            Role admin = existingAdminRole.get();
            RoleFunction roleFunction = RoleFunction.builder()
                    .roleId(admin.getId())
                    .functionId(newFunction.getId())
                    .build();
            roleFunctionRepository.save(roleFunction);
        }
        return modelMapper.map(functionRepository.save(newFunction), FunctionDTO.class);
    }

    @Override
    public FunctionDTO update(Long id, FunctionRequest functionRequest) {
        Function currentFunction = functionRepository.findById(id).orElseThrow(() -> {
            log.error("Function with id {} not found", id);
            throw new ApplicationException(Constant.ERROR_FUNCTION_NOT_FOUND);
        });
        Optional<Function> existingFunction = functionRepository.findByCode(functionRequest.getCode().toLowerCase());
        if (existingFunction.isPresent()) {
            log.error("Function with code {} already exists", functionRequest.getCode());
            throw new ApplicationException(Constant.ERROR_FUNCTION_NOT_FOUND);
        }
        currentFunction.setCode(functionRequest.getCode().toLowerCase());
        currentFunction.setName(functionRequest.getName());
        currentFunction.setDescription(functionRequest.getDescription());

        return modelMapper.map(functionRepository.save(currentFunction), FunctionDTO.class);
    }

    @Override
    public List<FunctionDTO> getAllFunction() {
        return functionRepository.findByIsDeletedIsFalse().stream()
                .map(function -> modelMapper.map(function, FunctionDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public FunctionDTO getFunction(Long id) {
        Function function = functionRepository.findById(id).orElseThrow(() -> {
            log.error("Function with id {} not found", id);
            throw new ApplicationException(Constant.ERROR_FUNCTION_NOT_FOUND);
        });

        if (function.getIsDeleted()) {
            throw new ApplicationException(Constant.ERROR_FUNCTION_NOT_FOUND);
        }

        return modelMapper.map(function, FunctionDTO.class);
    }

    @Override
    public void softDeleted(Long id) {
        Function function = functionRepository.findById(id).orElseThrow(() -> {
            log.error("Function with id {} not found", id);
            throw new ApplicationException(Constant.ERROR_FUNCTION_NOT_FOUND);
        });

        function.setIsDeleted(true);
        functionRepository.save(function);
    }

    @Override
    public void deleteFunction(Function function) {
        functionRepository.delete(function);
    }

    @Override
    public List<FunctionDTO> search(String keyword) {
        List<Function> functionDTOS = functionRepository.search(keyword);
        return functionDTOS.stream().map(function -> modelMapper.map(function, FunctionDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<FunctionDTO> importExcel(MultipartFile file) {
        try (InputStream input = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(input);
            Sheet sheet = workbook.getSheet("Functions");
            Iterator rowIterator = sheet.rowIterator();
            rowIterator.next();
            List<FunctionDTO> existingFunctions = functionRepository.findAll().stream()
                    .map(function -> modelMapper.map(function, FunctionDTO.class))
                    .toList();

            List<String> code = existingFunctions.stream().map(FunctionDTO::getCode).collect(Collectors.toList());

            List<Function> newFunctions = new ArrayList<>();
            while (rowIterator.hasNext()) {
                Row row = (Row) rowIterator.next();
                String newCode = row.getCell(0).getStringCellValue().toLowerCase();
                if (code.contains(newCode)) {
                    continue;
                }
                String newName = row.getCell(1).getStringCellValue().toLowerCase();
                String newDescription = row.getCell(2).getStringCellValue().toLowerCase();
                newFunctions.add(Function.builder()
                        .code(newCode)
                        .name(newName)
                        .description(newDescription)
                        .build());
                code.add(newCode);
            }
            List<Function> functions = functionRepository.saveAll(newFunctions);
            addFunctionToRoleAdmin(functions);
            return functions.stream().map(function -> modelMapper.map(function, FunctionDTO.class)).collect(Collectors.toList());

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ApplicationException(Constant.ERROR_FUNCTION_NOT_FOUND);
        }
    }

    @Override
    public byte[] exportExcel() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Functions");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Code");
            row.createCell(1).setCellValue("Name");
            row.createCell(2).setCellValue("Description");

            List<Function> functions = functionRepository.findAll();
            int i = 1;
            for (Function function : functions) {
                row = sheet.createRow(i);
                row.createCell(0).setCellValue(function.getCode());
                row.createCell(1).setCellValue(function.getName());
                row.createCell(2).setCellValue(function.getDescription());
                i++;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return out.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    private void addFunctionToRoleAdmin(List<Function> functions) {
        Optional<Role> adminRole = roleRepository.findByCode("admin");
        if (adminRole.isPresent()) {
            Role admin = adminRole.get();
            List<RoleFunction> roleFunctions = new ArrayList<>();
            for (Function function : functions) {
                roleFunctions.add(RoleFunction.builder()
                        .functionId(function.getId())
                        .roleId(admin.getId())
                        .build());
            }
            roleFunctionRepository.saveAll(roleFunctions);
        }
    }
}
