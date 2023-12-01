package org.acme.hibernate.orm.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.hibernate.orm.entity.Post;
import org.acme.hibernate.orm.entity.Tag;
import org.acme.hibernate.orm.repo.BlogRepository;

import java.util.List;

@Path("/api")
@Produces("application/json")
@Consumes("application/json")
public class BlogController {
    @Inject
    BlogRepository blogRepository;

    @Path("/posts")
    @GET
    public List<Post> getPosts() {
        return blogRepository.getPosts();
    }

    @Path("/posts/{id}")
    @GET
    public Post getPost(Integer id) {
        return blogRepository.getPost(id);
    }

    @Path("/posts")
    @POST
    public Response createPost(Post post) {
        post.setId(null);
        blogRepository.createPost(post);
        return Response.ok().build();
    }

    @Path("/posts/{id}")
    @PUT
    public Response updatePost(Integer id, Post post) {
        post.setId(id);
        blogRepository.updatePost(post);
        return Response.ok().build();
    }

    @Path("/posts/{id}")
    @DELETE
    public Response deletePost(Integer id) {
        blogRepository.deletePost(id);
        return Response.ok().build();
    }


    @Path("/tags")
    @GET
    public List<Tag> getTags() {
        return blogRepository.getTags();
    }

    @Path("/tags/{id}")
    @GET
    public Tag getTag(Integer id) {
        return blogRepository.getTag(id);
    }

    @Path("/tags")
    @POST
    public Response createTag(Tag tag) {
        tag.setId(null);
        blogRepository.createTag(tag);
        return Response.ok().build();
    }

    @Path("/tags/{id}")
    @PUT
    public Response updateTag(Integer id, Tag tag) {
        Tag oldTag = blogRepository.getTag(id);
        tag.setId(id);
        blogRepository.updateTag(tag, oldTag.getLabel());
        return Response.ok().build();
    }

    @Path("/tags/{id}")
    @DELETE
    public Response deleteTag(Integer id) {
        Tag oldTag = blogRepository.getTag(id);
        blogRepository.deleteTag(id, oldTag.getLabel());
        return Response.ok().build();
    }
}
