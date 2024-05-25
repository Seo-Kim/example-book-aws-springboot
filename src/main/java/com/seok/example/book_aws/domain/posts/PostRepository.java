package com.seok.example.book_aws.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository< PostEntity, Long > {

}
