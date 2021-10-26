package com.assessment.example.response;

import java.util.List;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorMessage {
    private final String errorCode;
    private final List<String> error;
}
