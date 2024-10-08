package com.nus.cool.core.cohort.birthselect;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nus.cool.core.cohort.filter.Filter;
import com.nus.cool.core.cohort.storage.ProjectedTuple;
import com.nus.cool.core.field.FieldValue;
import com.nus.cool.core.io.readstore.MetaChunkRS;
import java.util.List;
import lombok.Getter;

/**
 * EventSelection is a collection of filters
 * if one data item pass all filters in this Event X's eventSelection
 * it can be selected as Event X.
 * json mapper to constuct EventSelection
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
   * Judge whether this tuple is accepted by the event filter.
   *
   * @param projectTuple partial row of one data tuple
   * @return whether this item can be chosen as a birthEvents
   */
  public boolean accept(ProjectedTuple projectTuple) {
    for (int i = 0; i < filterList.size(); i++) {
      Filter filter = filterList.get(i);
      FieldValue v = projectTuple.getValueBySchema(filter.getFilterSchema());
      if (!filter.accept(v)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Initialize filters with meta chunk info.
   */
  public void loadMetaInfo(MetaChunkRS metachunk) {
    for (Filter f : filterList) {
      f.loadMetaInfo(metachunk);
    }
  }
}
