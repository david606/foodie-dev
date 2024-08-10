package com.imooc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@ToString
public class Carousel {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 图片地址
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * 背景颜色
     */
    @Column(name = "background_color")
    private String backgroundColor;

    /**
     * 商品id
     */
    @Column(name = "item_id")
    private String itemId;

    /**
     * 商品分类id
     */
    @Column(name = "cat_id")
    private String catId;

    /**
     * 轮播图类型，用于判断，可以根据商品id或者分类进行页面跳转，1：商品 2：分类
     */
    private Integer type;

    /**
     * 轮播图展示顺序，从小到大
     */
    private Integer sort;

    /**
     * 是否展示，1：展示 0：不展示
     */
    @Column(name = "is_show")
    private Integer isShow;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;
}
