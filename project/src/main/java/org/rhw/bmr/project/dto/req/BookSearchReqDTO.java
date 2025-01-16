package org.rhw.bmr.project.dto.req;

import lombok.Data;

@Data
public class BookSearchReqDTO {
    private String title;
    private String author;
    private String category;
    private String language;
}
