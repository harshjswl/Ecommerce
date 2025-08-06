package com.yg_threads.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponseDTO {
    private LocalDateTime timestamp;
    private int status;
   private String error;
   private Map<String,String> fieldErrors;
}
