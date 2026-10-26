package polymorphia.persistence.sqldb;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import polymorphia.artifacts.ArtifactType;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
class ArtifactRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("polymorphia")
            .withUsername("polymorphia")
            .withPassword("polymorphia");

    private SessionFactory createSessionFactory() {
        Properties props = new Properties();
        props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.put("hibernate.connection.url", postgres.getJdbcUrl());
        props.put("hibernate.hbm2ddl.auto", "create-drop");
        props.put("hibernate.connection.username", postgres.getUsername());
        props.put("hibernate.connection.password", postgres.getPassword());
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.hbm2ddl.auto", "create-drop");
        props.put("hibernate.show_sql", "false");

        return new Configuration()
                .addProperties(props)
                .addAnnotatedClass(ArtifactEntity.class)
                .buildSessionFactory();
    }

    @Disabled
    void saveAndLoadArtifact() {
        SessionFactory sessionFactory = createSessionFactory();
        ArtifactRepository repository = new ArtifactRepository(sessionFactory);

        ArtifactEntity saved = new ArtifactEntity(
                ArtifactType.Weapon,
                "Sword",
                0.0,
                0.0,
                0.0,
                3
        );

        Long id = repository.save(saved);
        assertNotNull(id);

        ArtifactEntity loaded = repository.findById(id);
        assertNotNull(loaded);
        assertEquals("Sword", loaded.getName());
        assertEquals(ArtifactType.Weapon, loaded.getType());
        assertEquals(3, loaded.getAttackValue());

        sessionFactory.close();
    }
}