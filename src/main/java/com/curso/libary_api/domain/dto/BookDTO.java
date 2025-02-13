package com.curso.libary_api.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {

    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    private String title;

    @NotBlank(message = "O autor é obrigatório")
    @Size(min = 3, max = 50, message = "O nome do autor deve ter entre 3 e 50 caracteres")
    private String author;

    @NotBlank(message = "O isbn é obrigatório")
    @Size(min = 3, max = 100, message = "O isbn deve ter entre 3 e 100 caracteres")
    private String isbn;


}
