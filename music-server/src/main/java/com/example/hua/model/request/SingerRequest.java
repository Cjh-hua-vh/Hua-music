package com.example.hua.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class SingerRequest {
    private Integer id;
    private String name;
    private Byte sex;
    private String pic;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birth;
    private String location;
    private String introduction;
}
