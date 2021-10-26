package com.assessment.example.mapper;

public interface RequestMapper<R, E> {
    E mapFrom(R request);
}