package dev.book.user.dto.request;

import dev.book.global.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record UserCategoriesRequest (

        @Schema(description = "변경하려고 하는 카테고리", defaultValue = "[\n" +
                "         \"food\", \"cafe_snack\", \"convenience_store\", \"shopping\", \"hobby\", \"living\", \"beauty\", \"health\"\n" +
                "   ]")
        List<String> categories
) {
}
