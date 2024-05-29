package com.seok.example.book_aws.entity.posts;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter  // 수정 시 명확한 의도를 반영하기 위해 Setter 없이 직접 메서드 작성
@EqualsAndHashCode( of="id" )
@NoArgsConstructor
@Entity
public class Post {  // camel case로 작성 시 snake case로 연결
    @Id
    // H2: "insert into post (author,content,title,id) values (?,?,?,default)"
    // MySQL: "insert into post (author,content,title) values (?,?,?)"
    @GeneratedValue( strategy=GenerationType.IDENTITY )  // IDENTITY = auto increment
    private Long id;

    @Column( length=500, nullable=false )  // length default = 255
    private String title;

    @Column( columnDefinition="TEXT", nullable=false )  // type default = VARCHAR
    private String content;

    private String author;

    @Builder  // 빌더 생성 (lombok)
    public Post( String title, String content, String author ) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Long update( String title, String content ) {
        this.title = title;
        this.content = content;
        return this.id;
    }
}
