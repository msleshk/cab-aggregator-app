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
    }

    @RollbackExecution
    public void rollback(MongoTemplate mongoTemplate) {
        mongoTemplate.dropCollection("ratings");
    }
}


