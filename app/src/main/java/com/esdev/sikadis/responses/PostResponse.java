package com.esdev.sikadis.responses;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PostResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private DataContainer dataContainer;

    @SerializedName("links")
    private Links links;
    @SerializedName("meta")
    private MetaInfo metaInfo;

    public boolean isSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public DataContainer getDataContainer() {
        return dataContainer;
    }


    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public static class DataContainer {

        @SerializedName("posts")
        private PostsData postsData;

        public PostsData getPostsData() {
            return postsData;
        }


    }

    public static class PostsData {
        @SerializedName("data")
        private List<Data> dataList;

        public List<Data> getDataList() {
            return dataList;
        }

        @SerializedName("links")
        private Links links;

        public Links getLinks() {
            return links;
        }

    }

    public static class Data {
        @SerializedName("title")
        private String title;
        @SerializedName("slug")
        private String slug;
        @SerializedName("content")
        private String content;
        @SerializedName("category")
        private String category;
        @SerializedName("thumbnail")
        private String thumbnail;
        @SerializedName("created_at")
        private String createdAt;

        public String getTitle() {
            return title;
        }

        public String getSlug() {
            return slug;
        }

        public String getContent() {
            return content;
        }

        public String getCategory() {
            return category;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }

    public static class Links {
        @SerializedName("first")
        private String first;
        @SerializedName("last")
        private String last;
        @SerializedName("prev")
        private String prev;
        @SerializedName("next")
        private String next;

        public String getFirst() {
            return first;
        }

        public String getLast() {
            return last;
        }

        public String getPrev() {
            return prev;
        }

        public String getNext() {
            return next;
        }
    }

    public static class MetaInfo {
        @SerializedName("current_page")
        private int currentPage;
        @SerializedName("from")
        private int from;
        @SerializedName("path")
        private String path;
        @SerializedName("per_page")
        private int perPage;
        @SerializedName("to")
        private int to;

        public int getCurrentPage() {
            return currentPage;
        }

        public int getFrom() {
            return from;
        }

        public String getPath() {
            return path;
        }

        public int getPerPage() {
            return perPage;
        }

        public int getTo() {
            return to;
        }
    }
}

