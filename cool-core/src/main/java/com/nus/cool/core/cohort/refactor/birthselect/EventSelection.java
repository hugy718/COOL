package com.nus.cool.core.cohort.refactor.birthselect;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nus.cool.core.cohort.refactor.filter.Filter;
import com.nus.cool.core.cohort.refactor.filter.FilterType;
import com.nus.cool.core.cohort.refactor.storage.ProjectedTuple;

import java.util.List;

import lombok.Getter;

/**
 * EventSelection is a collection of filters if one data item pass all filters
 * in this Event X's eventSelection it can be selected as Event X. json mapper
 * to constuct EventSelection
 */
public class EventSelection {
  // a list of filter, which generated by filter layout
  @JsonIgnore
  @Getter
  private final List<Filter> filterList;

  public EventSelection(List<Filter> filterList) {
    this.filterList = filterList;
  }

  /**
   * Check if the data item pass event criteria.
   * 
   * @param projectTuple partial row of a data tuple
   * @return whether this item can be chosen as a birthEvents
   */
  public boolean accept(ProjectedTuple projectTuple) {
    for (int i = 0; i < filterList.size(); i++) {
      Filter filter = filterList.get(i);
      if (filter.getType().equals(FilterType.Set)) {
        if (!filter.accept((String) projectTuple.getValueBySchema(filter.getFilterSchema()))) {
          return false;
        }
      } else if (filter.getType().equals(FilterType.Range)) {
        if (!filter.accept((Integer) projectTuple.getValueBySchema(filter.getFilterSchema()))) {
          return false;
        }
      } else {
        throw new IllegalArgumentException("Only support set or range");
      }
    }
    return true;
  }
}
