// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;
import com.google.sps.Event;
import com.google.sps.TimeRange;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set; 
import java.util.List;
import java.util.ArrayList;

public final class FindMeetingQuery {

  
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    //throw new UnsupportedOperationException("TODO: Implement this method.");
    
    Iterator<Event> iterator = events.iterator();
    List<TimeRange> answer = new ArrayList<TimeRange>();

    System.out.println (request.getDuration());
    System.out.println(TimeRange.WHOLE_DAY.duration());

    if (events.isEmpty()) {

        answer.add(TimeRange.WHOLE_DAY);

    } else if (request.getDuration()> TimeRange.WHOLE_DAY.duration()) {

    System.out.println("Request Duration too large");
    
    
    } else {
    
    while (iterator.hasNext()) {
    
    Event event = iterator.next();
    
    //System.out.println("The request attendees list is "+request.getAttendees());
    //System.out.println("The event attendees list is "+event.getAttendees());

    //System.out.println("The request duration is "+request.getDuration());
    //System.out.println("The event duration is "+event.getWhen().duration());
    

    //request.getAttendees() == event.getAttendees() &&
    //request.getDuration() >= event.getWhen().duration()

    if (request.getDuration() <= event.getWhen().duration()){

        answer.add(event.getWhen());
    }

    }

    }
    

  return answer;
}

}
