package ru.t1.java.demo.model.log;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "data_source_error_log")
public class DataSourceErrorLog extends AbstractPersistable<Long> {

    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    @Column(name = "message")
    private String message;

    @Column(name = "method_signature")
    private String methodSignature;
}