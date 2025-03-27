package com.emobile.springtodo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO для создания и обновления задачи")
public class TaskDto {
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(min = 1, max = 255, message = "Заголовок должен быть от 1 до 255 символов")
    @Schema(description = "Заголовок задачи", example = "Купить молоко")
    private String title;

    @NotBlank(message = "Описание не может быть пустым")
    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    @Schema(description = "Описание задачи", example = "Купить 2 литра молока")
    private String description;

    @Schema(description = "Статус выполнения задачи", example = "false")
    private boolean completed;
}