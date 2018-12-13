<nav class="navbar navbar-expand-lg navbar-dark" style="background-color: #666666;">
			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
			     <span class="navbar-toggler-icon"></span>
		    </button>
	     	<div class="collapse navbar-collapse" id="navbarNav">
	    		<ul class="navbar-nav">
					<?php
					 if (isset($_COOKIE["user"]))
					 {
						 echo'
        	 		 	<li class="nav-item active">
         	 				 <a class="nav-link" href="./main.html">Home <span class="sr-only">(current)</span></a>
						</li>
						<li class="nav-item">
							 <a class="nav-link" href="#" onclick="document.cookie = \'user\' + \'=;expires=Thu, 01 Jan 1970 00:00:01 GMT;\'; location.reload()" >Logout</a>
			 			</li>';
					 } else {
						 echo'
						<li class="nav-item">
         	 				 <a class="nav-link disabled">Home <span class="sr-only">(current)</span></a>
						</li>
						<li class="nav-item">
							 <a class="nav-link" href="./login.html">Login</a>
			 			</li>';
					 }
					 ?>
      		 		 <li class="nav-item">
		    	 		 <a class="nav-link" href="./register.html">Register</a>
		     		 </li>
		     		 
                     <li class="nav-item">
                         <a class="nav-link" href="./about.html">About</a>
                     </li>
		     	</ul>
  			</div>
		 	<a class="navbar-brand" href="#">
				 <img src="./images/user.png" width="30" height="30" alt="userIcon">
			</a>
</nav> 
