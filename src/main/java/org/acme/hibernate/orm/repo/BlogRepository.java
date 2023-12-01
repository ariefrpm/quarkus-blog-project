package org.acme.hibernate.orm.repo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.hibernate.orm.entity.Post;
import org.acme.hibernate.orm.entity.Tag;

import java.util.List;

@ApplicationScoped
public class BlogRepository {
    @Inject
    EntityManager em;
    public List<Post> getPosts() {
        String query = "SELECT \n" +
                "p.id AS id,\n" +
                "p.title AS title,\n" +
                "p.content AS content,\n" +
                "p.tags AS tags\n" +
                "FROM Post p ORDER BY p.id";

        return em.createNativeQuery(query, Post.class).getResultList();
    }

    public List<Tag> getTags() {
        String query = "SELECT \n" +
                "t.id AS id,\n" +
                "t.label AS label\n" +
                "FROM Tag t ORDER BY t.id";

        return em.createNativeQuery(query, Tag.class).getResultList();
    }

    public Post getPost(Integer postID) {
        String query = "SELECT \n" +
                "p.id AS id,\n" +
                "p.title AS title,\n" +
                "p.content AS content,\n" +
                "p.tags AS tags\n" +
                "FROM Post p WHERE p.id = :id";

        return (Post) em.createNativeQuery(query, Post.class)
                .setParameter("id", postID)
                .getSingleResult();
    }
    public Tag getTag(Integer tagID) {
        String query = "SELECT \n" +
                "t.id AS id,\n" +
                "t.label AS label\n" +
                "FROM Tag t WHERE t.id = :id";

        return (Tag) em.createNativeQuery(query, Tag.class)
                .setParameter("id", tagID)
                .getSingleResult();
    }

    @Transactional
    public void createPost(Post post) {
        String query = "insert into tag (id, label) values (nextval('Tag_SEQ'), :label) on conflict do nothing";
        em.persist(post);
        for(String s : post.getTags()) {
            em.createNativeQuery(query).setParameter("label", s).executeUpdate();
        }
    }

    @Transactional
    public void updatePost(Post post) {
        String query = "insert into tag (id, label) values (nextval('Tag_SEQ'), :label) on conflict do nothing";
        em.merge(post);
        for(String s : post.getTags()) {
            em.createNativeQuery(query).setParameter("label", s).executeUpdate();
        }
    }

    @Transactional
    public void deletePost(Integer postID) {
        String query = "delete from post where id = :id";
        em.createNativeQuery(query).setParameter("id", postID).executeUpdate();
    }

    @Transactional
    public void createTag(Tag tag) {
        em.persist(tag);
    }

    @Transactional
    public void updateTag(Tag tag, String oldTag) {
        em.merge(tag);
        //updating post tags can be asynchronous as my assumption it will be very rare event, and can be eventual consistent
        String query = "update post set tags[array_position(tags, :oldTag)] = :newTag where :oldTag = ANY (tags)";
        em.createNativeQuery(query)
                .setParameter("oldTag", oldTag)
                .setParameter("newTag", tag.getLabel())
                .executeUpdate();
    }
    @Transactional
    public void deleteTag(Integer tagID, String oldTag) {
        String query = "delete from tag where id = :id";
        em.createNativeQuery(query).setParameter("id", tagID).executeUpdate();
        //deleting post tags can be asynchronous as my assumption it will be very rare event, and can be eventual consistent
        String queryUpdate = "update post set tags = array_remove(tags, :oldTag) where :oldTag = ANY (tags)";
        em.createNativeQuery(queryUpdate)
                .setParameter("oldTag", oldTag)
                .executeUpdate();
    }
}
