<map version="0.9.0">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node BACKGROUND_COLOR="#ffcc66" COLOR="#000000" CREATED="1228811761115" ID="ID_295299344" MODIFIED="1229368583771" TEXT="Java Network Programming Project">
<font NAME="SansSerif" SIZE="20"/>
<hook NAME="accessories/plugins/AutomaticLayout.properties"/>
<node COLOR="#0033ff" CREATED="1228817062866" ID="ID_225755957" MODIFIED="1228817066596" POSITION="right" TEXT="Vision">
<edge STYLE="sharp_bezier" WIDTH="8"/>
<font NAME="SansSerif" SIZE="18"/>
<node COLOR="#00b439" CREATED="1229156033786" ID="ID_950209831" MODIFIED="1229156095565" TEXT="To write an application based on Fractal Component Programming model to support parameter sweep simulation programs.">
<edge STYLE="bezier" WIDTH="thin"/>
<font NAME="Aller" SIZE="16"/>
</node>
<node COLOR="#00b439" CREATED="1229156130332" ID="ID_1426575222" MODIFIED="1229156154304" TEXT="The simulation program can be an executable file with some sets of data files.">
<edge STYLE="bezier" WIDTH="thin"/>
<font NAME="Aller" SIZE="16"/>
</node>
<node COLOR="#00b439" CREATED="1229156249036" ID="ID_459276856" MODIFIED="1229156527069">
<richcontent TYPE="NODE"><html>
  <head>
    
  </head>
  <body>
    <p>
      In case of this project, we have implemented the simulation program ourselves and that is the multiplication of two huge matrices. But in general, it can use external executables and run them accordingly.
    </p>
  </body>
</html></richcontent>
<edge STYLE="bezier" WIDTH="thin"/>
<font NAME="Aller" SIZE="16"/>
</node>
</node>
<node COLOR="#0033ff" CREATED="1228817072602" ID="ID_736953458" MODIFIED="1229158287930" POSITION="right" TEXT="Architecture">
<edge STYLE="sharp_bezier" WIDTH="8"/>
<font NAME="SansSerif" SIZE="18"/>
<node COLOR="#00b439" CREATED="1229158287857" ID="ID_1979533231" MODIFIED="1229158291858" TEXT="Description">
<edge STYLE="bezier" WIDTH="thin"/>
<font NAME="Aller" SIZE="16"/>
<node COLOR="#990000" CREATED="1229156554041" ID="ID_1279730501" MODIFIED="1229158287880">
<richcontent TYPE="NODE"><html>
  <head>
    
  </head>
  <body>
    <p>
      The architecture of this application consists of three parts: <b>FrontEnd</b> component, <b>BagOfTasks</b> group and <b>Simulators</b> group.
    </p>
    <p>
      <b>BagOfTasks</b> and Simulators group consist of arbitrary number of <b>UserTask</b> and <b>Simulator</b> components.
    </p>
    <p>
      The <b>FrontEnd</b> component is connected to a GUI that is interacting with the user. The user can submit the input matrices with specifying the matrix file and then he should wait for the result.
    </p>
    <p>
      <b>FrontEnd</b> component is bound to <b>BagOfTasks</b> group as <i>one-to-any</i> binding. It means that any given time one of the components in <b>BagOfTasks</b> group is selected. Also the group <b>BagOfTasks</b> is bound to <b>Simulators</b> group as <i>one-to-any</i> binding.
    </p>
    <p>
      For feeding back the result to user, <b>Simulators</b> group has been bound to <b>FrontEnd</b> component as <i>one-to-one</i> binding thus when any simulator component is done with the simulation, it can announce the <b>FrontEnd</b> component.
    </p>
  </body>
</html></richcontent>
<edge STYLE="bezier" WIDTH="thin"/>
<font NAME="Aller" SIZE="14"/>
</node>
</node>
<node COLOR="#00b439" CREATED="1228817078737" ID="ID_61165920" MODIFIED="1229158284651" TEXT="Components">
<edge STYLE="bezier" WIDTH="thin"/>
<font NAME="SansSerif" SIZE="16"/>
<node COLOR="#990000" CREATED="1228817083989" ID="ID_392514686" MODIFIED="1229158284651" TEXT="FrontEnd">
<font NAME="SansSerif" SIZE="14"/>
<node COLOR="#111111" CREATED="1229157098000" ID="ID_34348757" MODIFIED="1229157813218" TEXT="Interfaces">
<node COLOR="#111111" CREATED="1229157132871" ID="ID_334805227" MODIFIED="1229157144419" TEXT="FEPublishTask (Client Interface)">
<node COLOR="#111111" CREATED="1229157205804" ID="ID_1832552345" MODIFIED="1229157216184" TEXT="Bound to BagOfTasks group"/>
</node>
<node COLOR="#111111" CREATED="1229157144789" ID="ID_1891362258" MODIFIED="1229157157948" TEXT="ResultAnnounce (Server Interface)">
<node COLOR="#111111" CREATED="1229157222088" ID="ID_707347439" MODIFIED="1229157232883" TEXT="Bound to Simulators group"/>
</node>
</node>
<node COLOR="#111111" CREATED="1229157612114" ID="ID_833996925" MODIFIED="1229157618318" TEXT="Responsiblity">
<node COLOR="#111111" CREATED="1229157620275" ID="ID_1496171954" MODIFIED="1229157720180" TEXT="Divides the input matrices in this way that a row and a column from the first and second matrix is considered a task. This task is submitted to UserTask queues."/>
</node>
</node>
<node COLOR="#990000" CREATED="1228817088911" ID="ID_11249360" MODIFIED="1229158284655" TEXT="UserTask">
<font NAME="SansSerif" SIZE="14"/>
<node COLOR="#111111" CREATED="1229157239329" ID="ID_837258075" MODIFIED="1229157805924" TEXT="Interfaces">
<node COLOR="#111111" CREATED="1229157247107" ID="ID_1574338271" MODIFIED="1229157259529" TEXT="FEPublishTask (Server Interface)">
<node COLOR="#111111" CREATED="1229157274089" ID="ID_1684453191" MODIFIED="1229157285983" TEXT="Bound to FrontEnd component"/>
</node>
<node COLOR="#111111" CREATED="1229157260176" ID="ID_156756475" MODIFIED="1229157272545" TEXT="UTPublishTask (Client Interface)">
<node COLOR="#111111" CREATED="1229157288135" ID="ID_1429414300" MODIFIED="1229157299716" TEXT="Bound to Simulators group"/>
</node>
</node>
<node COLOR="#111111" CREATED="1229157724837" ID="ID_1004185763" MODIFIED="1229157728515" TEXT="Responsblity">
<node COLOR="#111111" CREATED="1229157731672" ID="ID_706088151" MODIFIED="1229157772109" TEXT="Submits every task to one of the simulators in Simulator group. "/>
</node>
</node>
<node COLOR="#990000" CREATED="1228817094335" ID="ID_364918529" MODIFIED="1229158284659" TEXT="Simulator">
<font NAME="SansSerif" SIZE="14"/>
<node COLOR="#111111" CREATED="1229157304300" ID="ID_1247603376" MODIFIED="1229157800857" TEXT="Interfaces">
<node COLOR="#111111" CREATED="1229157348564" ID="ID_105196173" MODIFIED="1229157360558" TEXT="UTPublishTask (Server Interface)">
<node COLOR="#111111" CREATED="1229157373677" ID="ID_945126256" MODIFIED="1229157396101" TEXT="Bound to BagOfTasks group"/>
</node>
<node COLOR="#111111" CREATED="1229157360780" ID="ID_1578465980" MODIFIED="1229157370952" TEXT="ResultAnnounce (Client Interface)">
<node COLOR="#111111" CREATED="1229157398352" ID="ID_933520339" MODIFIED="1229157405422" TEXT="Bound to FrontEnd component"/>
</node>
</node>
<node COLOR="#111111" CREATED="1229157780258" ID="ID_831378979" MODIFIED="1229157784239" TEXT="Responsblity">
<node COLOR="#111111" CREATED="1229157816755" ID="ID_1065860526" MODIFIED="1229157881416" TEXT="Computes the final element of the final matrix from the received row and column and send back the result to the FrontEnd component"/>
</node>
</node>
</node>
<node COLOR="#00b439" CREATED="1229368461951" FOLDED="true" ID="ID_1654134319" MODIFIED="1229368745747" TEXT="">
<edge STYLE="bezier" WIDTH="thin"/>
<font NAME="Aller" SIZE="16"/>
<icon BUILTIN="attach"/>
<node COLOR="#990000" CREATED="1229368478912" ID="ID_276572353" MODIFIED="1229368561582">
<richcontent TYPE="NODE"><html>
  <head>
    
  </head>
  <body>
    <img src="NPJ_report_images/report_comp_arch" />
  </body>
</html></richcontent>
<font NAME="Aller" SIZE="14"/>
</node>
</node>
</node>
<node COLOR="#0033ff" CREATED="1228819281529" ID="ID_1333746193" MODIFIED="1228819296311" POSITION="right" TEXT="Simulation">
<edge STYLE="sharp_bezier" WIDTH="8"/>
<font NAME="SansSerif" SIZE="18"/>
<node COLOR="#00b439" CREATED="1228819298037" ID="ID_1623566067" MODIFIED="1228819299781" TEXT="Input">
<edge STYLE="bezier" WIDTH="thin"/>
<font NAME="SansSerif" SIZE="16"/>
<node COLOR="#990000" CREATED="1229157412330" ID="ID_1525116293" MODIFIED="1229157473551" TEXT="A file consists of two matrices. For each one, the first line is the dimension of the matrix and the next lines are the elements of the matrix">
<font NAME="Aller" SIZE="14"/>
</node>
</node>
<node COLOR="#00b439" CREATED="1228819300017" ID="ID_899858228" MODIFIED="1228819301994" TEXT="Output">
<edge STYLE="bezier" WIDTH="thin"/>
<font NAME="SansSerif" SIZE="16"/>
<node COLOR="#990000" CREATED="1229157476541" ID="ID_1088700024" MODIFIED="1229157503745" TEXT="Generated on the GUI as a table indicating the produced matrix">
<font NAME="Aller" SIZE="14"/>
</node>
</node>
</node>
<node COLOR="#0033ff" CREATED="1228817100557" ID="ID_975711521" MODIFIED="1228817105286" POSITION="right" TEXT="Future Works">
<edge STYLE="sharp_bezier" WIDTH="8"/>
<font NAME="SansSerif" SIZE="18"/>
<node COLOR="#00b439" CREATED="1228817388171" ID="ID_1222608422" MODIFIED="1228817398994" TEXT="Performance Improvement">
<edge STYLE="bezier" WIDTH="thin"/>
<font NAME="SansSerif" SIZE="16"/>
<node COLOR="#990000" CREATED="1228817499665" ID="ID_1295912022" MODIFIED="1228817502435" TEXT="Caching">
<font NAME="SansSerif" SIZE="14"/>
<node COLOR="#111111" CREATED="1229157973508" ID="ID_105126498" MODIFIED="1229158103019">
<richcontent TYPE="NODE"><html>
  <head>
    
  </head>
  <body>
    <p>
      UserTask components in BagOfTasks group can simply record that what rows and columns has been sent to each Simulator component. In this way, they can avoid sending again the previously sent row or column and save a lot of network bandwidth.
    </p>
  </body>
</html></richcontent>
</node>
</node>
</node>
<node COLOR="#00b439" CREATED="1228817507456" ID="ID_848468957" MODIFIED="1228817514104" TEXT="Direct Job Submitting">
<edge STYLE="bezier" WIDTH="thin"/>
<font NAME="SansSerif" SIZE="16"/>
<node COLOR="#990000" CREATED="1229158106594" ID="ID_1199336292" MODIFIED="1229158171036" TEXT="In some cases, UserTask components can be considered as redundant layer in this architecture. Thus we can have direct job submitting from FrontEnd component to Simulator groups.">
<font NAME="Aller" SIZE="14"/>
</node>
</node>
<node COLOR="#00b439" CREATED="1228817180924" ID="ID_873028484" MODIFIED="1229158192624" TEXT="Scheduler and Load Balancing">
<edge STYLE="bezier" WIDTH="thin"/>
<font NAME="SansSerif" SIZE="16"/>
<node COLOR="#990000" CREATED="1229158196266" ID="ID_345263143" MODIFIED="1229158330892">
<richcontent TYPE="NODE"><html>
  <head>
    
  </head>
  <body>
    <p>
      The main purpose of creating BagOfTasks group is for future extensions. We can implement scheduler and load balancer in this layer and avoid performance bottlenecks in a particular simulator component.
    </p>
  </body>
</html></richcontent>
<font NAME="Aller" SIZE="14"/>
</node>
</node>
</node>
<node COLOR="#0033ff" CREATED="1228817109953" ID="ID_1608552722" MODIFIED="1228817521651" POSITION="right" TEXT="Developers">
<edge STYLE="sharp_bezier" WIDTH="8"/>
<font NAME="SansSerif" SIZE="18"/>
<node COLOR="#00b439" CREATED="1228817116649" ID="ID_245396636" MODIFIED="1228817133114" TEXT="M. Amir Moulavi &lt;moulavi@kth.se&gt;">
<edge STYLE="bezier" WIDTH="thin"/>
<font NAME="SansSerif" SIZE="16"/>
</node>
<node COLOR="#00b439" CREATED="1228817133922" ID="ID_1369660246" MODIFIED="1228817156408" TEXT="Uwe Dauernheim &lt;uwe@dauernheim.se&gt;">
<edge STYLE="bezier" WIDTH="thin"/>
<font NAME="SansSerif" SIZE="16"/>
</node>
</node>
</node>
</map>
