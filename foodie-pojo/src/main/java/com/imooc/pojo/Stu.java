package com.imooc.pojo;

import lombok.*;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Builder
public class Stu {
    @Id
    private Integer id;

    private String name;

    private Integer age;
}