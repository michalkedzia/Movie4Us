package com.movie4us;

import data.PageMovieData;

public class Message {
  private String username;
  private String connectedUser;
  private String action;
  private String status;
  private String selectedCategory;
  private PageMovieData movies;
  private int movieId;

  public String getUsername() {
    return username;
  }

  public int getMovieId() {
    return movieId;
  }

  public void setMovieId(int movieId) {
    this.movieId = movieId;
  }

  @Override
  public String toString() {
    return "Message{"
        + "username='"
        + username
        + '\''
        + ", connectedUser='"
        + connectedUser
        + '\''
        + ", action='"
        + action
        + '\''
        + ", status='"
        + status
        + '\''
        + ", selectedCategory='"
        + selectedCategory
        + '\''
        + ", movies="
        + movies
        + '}';
  }

  public String getSelectedCategory() {
    return selectedCategory;
  }

  public PageMovieData getMovies() {
    return movies;
  }

  public void setMovies(PageMovieData movies) {
    this.movies = movies;
  }

  public void setSelectedCategory(String selectedCategory) {
    this.selectedCategory = selectedCategory;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getConnectedUser() {
    return connectedUser;
  }

  public void setConnectedUser(String connectedUser) {
    this.connectedUser = connectedUser;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
