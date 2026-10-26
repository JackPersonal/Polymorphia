package polymorphia.persistence.sqldb;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class ArtifactRepository {
    private final SessionFactory sessionFactory;

    public ArtifactRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Long save(ArtifactEntity artifact) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(artifact);
            session.flush();
            tx.commit();
            return artifact.getId();
        }
    }

    public ArtifactEntity findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(ArtifactEntity.class, id);
        }
    }
}