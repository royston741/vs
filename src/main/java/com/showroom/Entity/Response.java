package com.showroom.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private boolean isSuccess;
    private List<String> errMessage = new ArrayList<>();
    private Object responseData;

    public Response(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
