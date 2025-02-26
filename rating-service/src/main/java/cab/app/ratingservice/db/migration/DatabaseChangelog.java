package cab.app.ratingservice.db.migration;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id = "init-database", order = "001", author = "Maksim Leshko")
public class DatabaseChangelog {
    @Execution
    public void migrate(MongoTemplate mongoTemplate) {
        if (!mongoTemplate.collectionExists("ratings")) {
            mongoTemplate.createCollection("ratings");
        }
        mongoTemplate.insert(new Document()
                .append("rideId", 123)
                .append("userId", 456)
                .append("userRole", "PASSENGER")
                .append("rating", 5)
                .append("comment", "Great ride!"), "ratings");

        mongoTemplate.insert(new Document()
                .append("rideId", 789)
                .append("userId", 321)
                .append("userRole", "DRIVER")
                .append("rating", null)
                .append("comment", null), "ratings");
    }

    @RollbackExecution
    public void rollback(MongoTemplate mongoTemplate) {
        mongoTemplate.dropCollection("ratings");
    }
}


