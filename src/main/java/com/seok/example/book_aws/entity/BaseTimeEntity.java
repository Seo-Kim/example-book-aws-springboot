package com.seok.example.book_aws.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners( AuditingEntityListener.class )
public class BaseTimeEntity {
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column( updatable=false )
    private Long createId;
    private Long modifyId;

    public void create( Long createId ) {
        this.createId = createId;
        this.modifyId = createId;
    }

    public void modify( Long modifyId ) {
        this.modifyId = modifyId;
    }
}