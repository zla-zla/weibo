package com.example.weibo.entity.response;

import com.example.weibo.entity.Post;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PostListResponse implements Serializable {

    @SerializedName("content")
    private List<Post> content;
    @SerializedName("pageable")
    private PageableDTO pageable;
    @SerializedName("last")
    private Boolean last;
    @SerializedName("totalPages")
    private Integer totalPages;
    @SerializedName("totalElements")
    private Integer totalElements;
    @SerializedName("size")
    private Integer size;
    @SerializedName("number")
    private Integer number;
    @SerializedName("sort")
    private SortDTO sort;
    @SerializedName("numberOfElements")
    private Integer numberOfElements;
    @SerializedName("first")
    private Boolean first;
    @SerializedName("empty")
    private Boolean empty;

    public List<Post> getContent() {
        return content;
    }

    public void setContent(List<Post> content) {
        this.content = content;
    }

    public PageableDTO getPageable() {
        return pageable;
    }

    public void setPageable(PageableDTO pageable) {
        this.pageable = pageable;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public SortDTO getSort() {
        return sort;
    }

    public void setSort(SortDTO sort) {
        this.sort = sort;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    public static class PageableDTO {
        @SerializedName("sort")
        private SortDTO sort;
        @SerializedName("offset")
        private Integer offset;
        @SerializedName("pageSize")
        private Integer pageSize;
        @SerializedName("pageNumber")
        private Integer pageNumber;
        @SerializedName("unpaged")
        private Boolean unpaged;
        @SerializedName("paged")
        private Boolean paged;

        public SortDTO getSort() {
            return sort;
        }

        public void setSort(SortDTO sort) {
            this.sort = sort;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Integer getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(Integer pageNumber) {
            this.pageNumber = pageNumber;
        }

        public Boolean getUnpaged() {
            return unpaged;
        }

        public void setUnpaged(Boolean unpaged) {
            this.unpaged = unpaged;
        }

        public Boolean getPaged() {
            return paged;
        }

        public void setPaged(Boolean paged) {
            this.paged = paged;
        }

        public static class SortDTO {
            @SerializedName("sorted")
            private Boolean sorted;
            @SerializedName("unsorted")
            private Boolean unsorted;
            @SerializedName("empty")
            private Boolean empty;

            public Boolean getSorted() {
                return sorted;
            }

            public void setSorted(Boolean sorted) {
                this.sorted = sorted;
            }

            public Boolean getUnsorted() {
                return unsorted;
            }

            public void setUnsorted(Boolean unsorted) {
                this.unsorted = unsorted;
            }

            public Boolean getEmpty() {
                return empty;
            }

            public void setEmpty(Boolean empty) {
                this.empty = empty;
            }
        }
    }

    public static class SortDTO {
        @SerializedName("sorted")
        private Boolean sorted;
        @SerializedName("unsorted")
        private Boolean unsorted;
        @SerializedName("empty")
        private Boolean empty;

        public Boolean getSorted() {
            return sorted;
        }

        public void setSorted(Boolean sorted) {
            this.sorted = sorted;
        }

        public Boolean getUnsorted() {
            return unsorted;
        }

        public void setUnsorted(Boolean unsorted) {
            this.unsorted = unsorted;
        }

        public Boolean getEmpty() {
            return empty;
        }

        public void setEmpty(Boolean empty) {
            this.empty = empty;
        }
    }

}
