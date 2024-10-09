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
@Table(name = "time_limit_exceed_log")
public class TimeLimitExceedLog extends AbstractPersistable<Long> {

    @Column(name = "method_signature")
    private String methodSignature;

    @Column(name = "execution_time")
    private Long executionTime;
}