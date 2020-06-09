
 
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

/**
 * Adds a random greeting to the page.
 */

// found in tutorial from https://www.youtube.com/watch?v=4YQ4svkETS0&t=7s
var i = 0; 			// Start Point
var images = [];	// Images Array
var time = 3000;	// Time Between Switch
	 
// Image List
images[0] = "./images/lavender.jpg";
images[1] = "./images/school.jpg";
images[2] = "./images/writing.jpg";

// Change Image
function changeImg(){
	document.slide.src = images[i];

	// Check If Index Is Under Max
	if(i < images.length - 1){
	  // Add 1 to Index
	  i++; 
	} else { 
		// Reset Back To O
		i = 0;
	}

	// Run function every x seconds
	//setTimeout("changeImg()", time);
}

// Run function when page loads
window.onload=changeImg;


//wk3pt2 + 3
function getText() {
  fetch('/data').then(response => response.text()).then((text) => {
    document.getElementById('text-container').innerText = text;
  });
}


//wk3pt6


function getComments() {
  
  document.getElementById("history").innerText = '';
  
  const maxval = document.getElementById('max').value;
  fetch('/data?max='+maxval).then(response => response.text()).then((comments) => {
  comments = comments.replace("[","").replace("]","").split(",");
  for (i = 0; i<comments.length; i++) {
    document.getElementById("history").appendChild(createListElement(comments[i]));
  }

  });

}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}



function clearComments(){
    const params = new URLSearchParams();
    fetch('/delete-data',{method: 'POST', body: params});
    
}
window.onload=getComments;