package org.rhw.bmr.project.dto.resp;

import lombok.Data;

@Data
public class BookSearchRespDTO {
    private Long ref_id;
    private String title;
    private String reference;
    private String author;
    private String category;
    private String description;
    private String language;
    private Long clickCount;
    private Long sortedOrder;
}
