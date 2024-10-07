package ch.supsi.webapp.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Success {
    private Boolean success;
}
