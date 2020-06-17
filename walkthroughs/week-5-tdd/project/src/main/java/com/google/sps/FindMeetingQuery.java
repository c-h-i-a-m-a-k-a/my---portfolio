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
import java.lang.Long;

public final class FindMeetingQuery {

  
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    //throw new UnsupportedOperationException("TODO: Implement this method.");
    
    Iterator<Event> iterator = events.iterator();
    List<TimeRange> answer = new ArrayList<TimeRange>();

    Set<String> reqAttendees = new HashSet<String>(request.getAttendees());

    
    if (request.getDuration()> TimeRange.WHOLE_DAY.duration()) {

        System.out.println("Requested Duration too large");
    
    
    } 
    else if (events.isEmpty()) {

        answer.add(TimeRange.WHOLE_DAY);

    } 
    else if (events.size() == 1){

        Event event = iterator.next();

        Set<String> eventAttendees = new HashSet<String>(event.getAttendees());


        if (reqAttendees.retainAll(eventAttendees)) {

            answer.add(TimeRange.WHOLE_DAY);

        }

        else if (event.getWhen().start() == TimeRange.START_OF_DAY){
            answer.add(TimeRange.fromStartEnd(event.getWhen().end(),TimeRange.END_OF_DAY+1,false));
        }

        else if (event.getWhen().end() == TimeRange.END_OF_DAY){
            answer.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY,event.getWhen().start(),false));

        }
        else {
            answer.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY,event.getWhen().start(),false));
            answer.add(TimeRange.fromStartEnd(event.getWhen().end(),TimeRange.END_OF_DAY+1,false));

        }

    }

    else{

    Event event1 = iterator.next();
    Event event2 = iterator.next();
    
    Set<String> event1Attendees = new HashSet<String>(event1.getAttendees());
    Set<String> event2Attendees = new HashSet<String>(event2.getAttendees());

    int intDuration = (int) request.getDuration();

    if (reqAttendees.containsAll(event1Attendees) && (reqAttendees.containsAll(event2Attendees))) {

    if (event1.getWhen().contains(event2.getWhen())){
        answer.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY,event1.getWhen().start(),false));
        answer.add(TimeRange.fromStartEnd(event1.getWhen().end(),TimeRange.END_OF_DAY+1,false));
    }
    else if (event2.getWhen().contains(event1.getWhen())){
        answer.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY,event2.getWhen().start(),false));
        answer.add(TimeRange.fromStartEnd(event2.getWhen().end(),TimeRange.END_OF_DAY+1,false));
    }
    else if (event1.getWhen().start()== TimeRange.START_OF_DAY && event2.getWhen().end()==TimeRange.END_OF_DAY+1){
        
        if ((TimeRange.fromStartEnd(event1.getWhen().end(),event2.getWhen().start(),false).duration())>= request.getDuration()) {
            answer.add(TimeRange.fromStartDuration(event1.getWhen().end(),intDuration  ));
        }

    }
    else if (event2.getWhen().start()== TimeRange.START_OF_DAY && event1.getWhen().end()==TimeRange.END_OF_DAY+1) {

        if ((TimeRange.fromStartEnd(event2.getWhen().end(),event1.getWhen().start(),false).duration())>= request.getDuration()) {
           
            answer.add(TimeRange.fromStartDuration(event2.getWhen().end(),intDuration ));
        }
        
    }
    else if (event1.getWhen().overlaps(event2.getWhen()) || event2.getWhen().overlaps(event1.getWhen()) || (reqAttendees.containsAll(event1Attendees) && reqAttendees.containsAll(event2Attendees) )){

        if(event1.getWhen().start() < event2.getWhen().start()){
            answer.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY,event1.getWhen().start(),false));

            if ((event2.getWhen().end()-event1.getWhen().end()) > request.getDuration()) {
                answer.add(TimeRange.fromStartDuration(event1.getWhen().end(), intDuration));
            }

            answer.add(TimeRange.fromStartEnd(event2.getWhen().end(),TimeRange.END_OF_DAY+1,false));

        }
        else {
            answer.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY,event2.getWhen().start(),false));

            if ((event1.getWhen().end()-event2.getWhen().end()) > request.getDuration()) {
                answer.add(TimeRange.fromStartDuration(event2.getWhen().end(),intDuration));
            }

            answer.add(TimeRange.fromStartEnd(event1.getWhen().end(),TimeRange.END_OF_DAY+1,false));

        }


    }
    }
    
    /**

    Will use later for scenarios that involve more than 2 events being considered.

    while (iterator.hasNext()) {
    
    Event event = iterator.next();
    
    //System.out.println("The request attendees list is "+request.getAttendees());
    //System.out.println("The event attendees list is "+event.getAttendees());

    }
    **/

    }
    

  return answer;
}

}
