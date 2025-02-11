package immotrend.com.immotrend.dtos.toView;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String firstname;
    private String email;
    private String role;
    private String photo;
    @JsonProperty("created_at")
    private Instant createdAt;
    @JsonProperty("updated_at")
    private Instant updatedAt;
}
