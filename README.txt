Files in directory must be in "a(1).mp4, a(2).mp4...a(10).mp4" style.
Directory doesn't contain anything besides "*.mp4" or any other video files.
There are two mods of work:
    1) html/htm-files mode (HTMLConfiguration);
    2) txt-file mode (TXTConfiguration).
In default is on html/hml-files mode, but it can switched manually to txt-file mode by changing in AutoRenamer class.
If in use txt-file mode work style of program changes. Must to configure list.txt file by example:
                                    lesson 1
                                    Work with Css
                                    01:25:03
                                    
                                    lesson 2
                                    HTMl tags
                                    02:02:35
                                    
                                    ........
There can be any "\r\n" as you like between two configurations. Program automatically find they and split to different file names
In this mode list.txt can consists in directory.