package cab.app.ratingservice.model;

import cab.app.ratingservice.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "ratings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    private String id;
    private Long rideId;
    private Long userId;
    private Long ratedUserId;
    private Integer rating;
    private String comment;
    private Role userRole;
    private LocalDateTime createdAt;
}
