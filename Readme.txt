                TempRecord for Windows Release Notes
                ====================================

Thank you for using TempRecord for Windows.  This text can be found
in the file README.TXT in the directory where you installed
TempRecord.

The file contains a brief history of changes to Temprecord and
additional notes on program operation or possible problems detected
after the release of this version.

TempRecord International Ltd.
Thu 05/07/2012

http://www.temprecord.com
info@temprecord.com
---------------------------------------------------------------------
Changes to 5.28.0.2471

   - ADDITION:  The option to set the view mode when a logger's data is read or
     a file is opened now includes a selection: "Don't change the view mode".  If
     this is selected then the view mode remains as it was when a logger is
     re-read.

   - FIX:  The date of the first sample in the summary view should now display
     correctly.

   - ADDITION:  The application title bar now displays the version and build
     of the software.

   - IMPROVED:  The summary display gives a clearer indication of the status
     of the logger.

   - ADDITION:  There is now an option to lock the toolbars in place.  The
     position and state (whether docked or floating) cannot be changed unless
     this option (on the General tab of the Option dialog) is unchecked.

   - ADDITION:  support is now included for Temprecord loggers fitted with sensors
     suitable for monitoring cryogenic envionments, and also high temperature
     environments.  Contact your distributor for more details.

   - CHANGE:  The start delay is no longer set to a default value when the
     logger is reused, and the start value that was programmed into the logger
     is available and displayed on the summary view, and in printed reports.

     In order for this functionality to work, the logger must be programmed and
     read with version 5.28.0.2360 or later.

   - IMPROVED: TRW now remembers that serial number associated with the last
     dataset to be saved to a file and thus should no longer present unnecessary
     "Do you want to save the data to a file?" prompts.

   - FIX:  Pressing the Esc key when a "Verify" dialog (a dialog asking a
     "Yes/No" question) is displayed is now interpreted as "Cancel", rather
     than "No".

   - FIX:  A program execution error should no longer result when exiting the
     parameters dialog after setting the parameters for a Mon-T logger when
     the option to display the Mon-T-specific parameters is disabled.

   - ADDITION:  An option has been provided to allow Temprecord to use an
     "internal" SMTP server for emails.  In fact the email is sent using the
     an SMTP server managed by Temprecord.  If this option is
     selected the user does not have to provide any additional SMTP details,
     or have access to an SMTP server of their own.

     This option is now the installation default.

   - ADDITION:  An event is now logged whenever a logger is read, a .TR data
     file is saved, a PDF report is saved, or an Excel file is exported.  Events
     are also logged when file saves are unsuccessful, and there are read
     errors when reading the data from a logger.  

   - FIX:  Installation of TRW could create spurious non-functional
     entries in the Windows Explorer context menu.
     
   - CHANGE: The "Kiosk" version of Temprecord now has separate application
     data and temporary file folders.  TRW and TRW Kiosk can now be installed
     on the same PC without getting in each other's way.

   - ADDITION: The Web Options tab now displays the type of internet
     connection that was detected (one of LAN, Modem, Proxy, Modem Busy,
     or Unknown).

   - FIX: The application now remembers the last folder used for an "Open File"
     operation.

   - FIX: There was an error in the reporting of statistics. If the first
     sample in a record was the maximum value in that record (no more samples
     exceed that value) then the time and date of the maximum sample was
     reported incorrectly.

     The same applied to the minimum sample, when the first sample in the
     record was the minimum value for the whole record.

   - FIX: When accessing the parameters of a logger protected with a password,
     it was still possible to open the parameters dialog under some circumstances.
     (it was still not possible to save the parameters again however).
     This has been remedied.

   - FIX: When a logger was in the start delay mode or logging samples, it was
     still possible to open the parameters dialog under some circumstances.
     (it was still not possible to save the parameters again however).
     This has been remedied.

   - ADDITION: The Help menu now includes an entry to start a browser
     window positioned at the index page of the web-based help.

   - ADDITION: All help topics now contain a link to the equivalent help
     topic in the browser-based web help, so that you can check the web
     help to see if there is any updated help available for any topic.

   - CHANGE: The batch example files have been updated to include the use of the
     installation command-line parameters for setting the installation type amd
     importing of existing INI file settings.

   - FIX: A bug in CHM help prevented text inside tables from formatting
     properly when the help window was resized.

   - ADDITION: When viewing the Options/System tab, it is now possible to
     right-click a file or folder name and open an explorer window at that
     folder.  If the name corresponds to a file, and it is a .TXT, .INI, .PDF
     or .CHM file, it is also possible to open the file from this menu.

   - FIX: When installing over the top of Builds 765 and prior, the settings
     for Default Sample Period and Defsult Start Delay should be preserved.

   - CHANGE: Durations now appear in the exported spreadsheets as H:mm:ss
     where the hours can be any number, e.g. a duration of 1 week will show
     as 168:00:00, and the value will still be available as A DateTime float
     value in the spreadsheet.

   - FIX: Under some circumstances the displayed logging duration was incorrect
     when humidity loggers were being programmed.

   - FIX: Sequence number (inserted into a file or folder name when the "#"
     character is used) is now preserved when TRW exits.

   - CHANGE: The display of warnings in the parameters dialog.  has been
     improved.  A warning message and associated flashing icon is displayed if:

      - the temperature limits are outside the range of the logger.
      - the Mon-T limits are outside the min and max temperatures.
      - the "Start at time and date" date is not in the future

   - ADDITION: Added help text about rounding of exported values.

   - ADDITION: The bug reporting and error report dialog has been improved.

   - ADDITION: "Export" can now be accessed from the right-click menu.

   - ADDITION: A warning is displayed if the recorded data appears to be
     saturated at the upper or lower limit for the logger.

   - FIX: The "description" section of a product integrity report section
     is now word-wrapped and will no longer be truncated at the right-hand
     edge of the page.

   - FIX: Installation now takes place into the correct "Program Files" folder
     in foreign language versions of Windows.

   - FIX: Under rare circumstances a spurious warning about calibration data
     being invalid would display.

   - ADDITION: Functions have been added to allow start and end sections
     on the graph to be defined with the mouse.

   - ADDITION: Shift-Copy now copies the mean value of the samples between
     the start and end samples to the clipboard.

   - CHANGE: The startup log is now written to a folder off the local
     application data folder (it was previously written to a folder off the
     application's Program Files folder).

   - ADDITION: The last 10 startup logs are now retained in the
     LocalAppData.. Startup Logs folder.  The last 10 installation logs are also
     retained in the LocalAppData.. Installation Logs folder.  Finally, The last
     10 error logs are retained in the LocalAppData.. Error Logs\ folder.  The
     log files are all filename date-stamped and the installation log files are
     also filename version-stamped.  These log files are added as attachments
     to the email sent when a diagnostic report or error report is sent.

  - CHANGE: The installer has been altered so that the user can choose to:

      - install for just me (settings stored in user's local profile)
      - install for all users  (settings stored in user's local profile)
      - install for all users (shared settings stored in shared docs)
      - install for remote users (shared settings stored in EXE folder)

    The installation type is now displayed in the "Help/About" window.

    The installer will also display a page enabling the user to choose to import
    the settings from an existing INI file if one is found.

  - FIX: Sometimes a privileged instruction exception resulted when TRW
    tried to find out the number of installed printers on startup.  This
    was due to a bug in the printer API routines and has been worked around.

  - FIX: Sometimes an exception resulted if TRW could not write the INI file.

  - FIX: Sometimes an exception resulted if there was an operating system
    error opening the "Save file" dialog.

  - ADDITION: Added a button to the exception handler to exit the application.
    This is needed because if an exception occurs on exit, it was not
    possible to exit the application.

  - FIX: Exception sometimes occurred when closing the serial port when
    exiting TRW.

---------------------------------------------------------------------
Changes to 5.27

   - start-to-end sample regions can now be quickly defined with the
     mouse.  To select a range of samples with the mouse:

       - click on the graph where you would like the start sample to be.
       - with the shift key held down, click on the graph where you would
         like the end sample to be.

   - holding the shift key down and selecting Copy from the right-click
     menu will copy the mean temperature (and/or humidity) between the
     start and end samples to the clipboard.

   - the parameters dialog now displays a warning if the temperature
     limits are set outside the recording range of the logger.

   - added a file menu entry to close all open child windows.

   - under some circumstances, the parameters dialog displayed recording
     durations that were incorrect when humidity loggers were being
     programmed.  This has been fixed.  Note that only the displayed
     duration was incorrect - the operation of the loggers was unaffected.

   - minor functional and cosmetic improvements to Mon-T logger
     support.

   - fixes to date and time format when printed on graph time axis.  Date
     and time are now formatted according to the user's Control Panel settings.

   - default filetype for exported ASCII files now saved correctly.

   - number of samples and logger full status now correctly shown in
     saved files for Mon-T loggers.

   - fixes to event log display.

   - fixes to printing of PDF reports on monochrome printers.  A
     "Monochrome printer" option has been added, with optional
     gray-scale support.

   - fix to display units in PDF values report.

   - event log is now positioned at most recent entry on startup.

   - an existing INI file is now always replaced at installation (Kiosk
     version only)
     
   - legacy USB driver files are now removed during installation.  
     They can cause problems whem mixed with newer driver files.  

   - fixed possible crash if TRW shut down while web thread was 
     still underway.  

   - PDF reports now always use the paper size and orientation 
     specified in Options.  The printer output is formatted 
     as for the current printer settings, unless there is no 
     default printer, in which case the PDF settings are used.  
     
   - web/email jobs are now not started if there is no Internet
     connection found.  
     
   - fixes to annotation of X (time) axis in printed output
     and PDF reports

   - main window is now contrained to always appear on screen at
     startup.  
     
   - fixed error on startup with default printers that did not 
     support the interrogation of the paper orientation.

   - added option to determine whether datestamp used is the current 
     date and time or the first sample time or last sample time of 
     the logger when new filenames are generated.

   - added "z" and "o" formatting characters for including the
     timezone name and offset in formatted file and folder names.  

   - status message displays when menu function is attempted that 
     is not possible in the current context.    
     
   - additions of more statistical to export to Excel function.
   
   - addition of named ranges to export to Excel function.

   - status bar hints added to Options dialog.
   
   - speed button to select Graph mode view added.
   
   - minor cosmetic fixes to printed report pagination.
   
   - added options to allow the serial number and shipping data 
     to be included in the graph section of the printed/PDF report.

   - improvements to the formatting of paragraph data in reports.  
   
   - the initial view mode (temperature, humidity, or both) is now 
     set from the way the logger was configured.
     
   - added flag to force printed output to use the same colours as 
     specified for the screen output.  
     
   - printed reports headers and footer are now user-specified.  It
     is also possible to specify "meta-strings" such as %Page% in the
     header and footer.  These meta-strings are replaced at report
     generation time.

   - the initial load of a disk file after TRW starts sometimes sets
     the zoom factor inappropriately.  This has been fixed.  The 
     graph is now always zoomed so that the entire record is visible.

   - extensive changes to the formatting of displayed and printed 
     tabular values.  Values can now be optionally printed with
     units, degrees symbol, parentheses and commas.

   - the standard deviation of the entire sample set, the samples visible
     on the screen, and the samples between the start and end markers 
     is now calculated and displayed or printed in statistics reports.
     
   - proportional fonts can now be used for values (tabular) view.

   - fix to pagination to avoid overflow into the footer area.

   - the shortcut key Ctrl-O can now be used in a data window to
     open the Options dialog with the tab selected appropriate to
     the current view mode.

   - operating system should now display correctly for Windows 7

   - a problem has been identified when running on Windows 64-bit
     (platforms such as Windows Sever 2008 64-bit) when printing
     reports to some printers.  A bug in the printer drivers can
     cause TRW to fail with an error.  This should now not occur.

   - Reader interface COM port is now not left closed between logger
     accesses.  This means that Temprecord can coexist better with other
     applications using the same COM port, and the COM port is no
     longer left in an unstable state if the USB reader is unplugged
     during Temprecord operation.

   - default Meta-String for page number is now correct.

   - button added to send a diagnostics report to Temprecord.

   - web files are now added to the most-recently-used files list.  A
     function has been added to clear this list, and the maximum length
     has been increased to 20 files.

   - fixed a problem where reuse of loggers in auto mode sometimes
     erroneously reported as failed when files were printed and/or
     emailed.

   - minor additions to help text.

   - context-sensitive help how functions correctly when an item
     in the left-hand tree view of the options dialog is clicked on
     and F1 then pressed.

   - speed buttons have been added to the graph toolbar to move to
     the first and last sample.

   - the meta-strings "%Name%" and "%Email%" in your email body now function
     correctly (in previous versions the name was output for both cases).

   - "Grid error out of range" exception when mouse wheel was used in an unused
     area of the event log has been fixed.

   - startup log is now written to the Windows temporary files folder.

   - fixed error that could occur if installation was aborted befire any files
     had been copied.

   - fixed error that could occur if the print button was double-clicked when
     a long print job was specified.

   - the graph toolbar has been separated into two - there is a separate
     "Copy" toolbar which has the Select All, Set Start Sample, Set End Sample
     Copy to CLipboard and Copy to Excel speed buttons.

   - the state (docked/undocked) and position of the toolbars is now saved
     when Temprecord exits and restored again when it starts.

   - the "Go to sample" function is now implemented on its own toolbar.

   - the toolbar positions are now constrained to be within the current monitor
     desktop work area on startup.  This prevents the problem of toobars that
     can "disappear" when Temprecord is occasionally run on systems with
     multiple monitors.

   - the currently selected COM port is now displayed on the status bar.

   - if the Ctrl key is held down in graph view, the mousewheel can be used
     to zoom about the mouse cursor.

   - improvements made to installation on Windows 64-bit platforms (Windows 7
     and Vista).

   - installation script now correctly identifies 32-bit Vista systems.

   - fixed up numerous dead help links on the menus.

   - added a close "X" button to the info pane.

   - the chevron character on the "Goto sample" toolbar did not display
     correctly under some Windows platforms.  Fixed.

   - display of remaining battery for Mon-T loggers has been added.  Also
     graphics have been added to the summary screen and printed report when
     the battery remaining reaches critical levels.
     
--------------------------------------------------------------------
Changes to 5.26

   - support for new Temprecord Mon-T logger added.

   - button added to logger parameters dialog to save the current
     parameter set to the defaults.

   - changes made to folder organisation and permissions to improve
     operation in environment with limited Windows users.

   - added "U" formatting character to filename and folder
     specification to allow embedding of logged-in Windows user name.

   - fix to email addressing when more than one destination address specified. 

---------------------------------------------------------------------
Changes to 5.25

   - statistics now also presented for samples in the visible graph window.

   - options added for copying logger serial number and shipping data
     to clipboard or spreadsheet file.

   - added /Kiosk mode to allow starting Temprecord in a reduced-
     capability mode.

   - added /Auto mode to force Temprecord to open the auto mode window
     after startup.

   - pressing spacebar causes the logger in the reader to do a demand
     temperature conversion and display it in the summary screen.

   - installation changed so that the application settings are common to
     all users.

   - revamp of printing.

   - added print preview screen to print dialog.

   - added preview PDF in Acrobat.

   - added support for calibration expiry.

   - added functions to save printed report to PDF file.

   - added functions to email PDF reports.

   - added SMTP queued emails for installations without MAPI support.

   - added batch installer

   - added configurable formatted filenames and folders.

   - added ability to suppress most prompts.

---------------------------------------------------------------------
Changes to 5.24

   - added functions to select all the samples, copy selected samples
     to the clipboard, and copy selected samples to an Excel spreadsheet
     file.  Toolbar buttons for these functions have been added.

   - Go To Sample function now behaves if sample numbers outside the
     range of logged samples are entered.

   - right-mouse click function to copy samples between start and end
     to clipboard.

   - samples between start and end markers are shown on the graph with
     a different background colour.


---------------------------------------------------------------------
Changes to 5.23

   - fixed problem with naming of attachments in emailed files.

   - support added for Bluetooth-connected readers

   - support added for faster download with new firmware revision 2.14 logger.

     Note that the new loggers will only work with version 5.23 and above of
     Temprecord for Windows.  Previous revisions of loggers which do not
     support the faster download can sill be used with the newer version of
     TRW.

   - some users have reported problems with reusing loggers that have
     a password set.  This has been remedied.

   - fixed error when trying to use the Search Help function.

   - fixes to calculation of Refrigeration Index.

---------------------------------------------------------------------
Changes to 5.22

   - 'Topic Search' function in help menu now works.
   
   - any time a sample mumber is reported in statistics, the temperature
     at that sample is also reported.

   - reorganised the enables for the various statistical functions so 
     that the one enable controls display on the graph, the printed 
     graph, the statistics, and the exported data. 

   - a function has been added to go to any sample number.     

   - added Product Integrity Profile display.
   
---------------------------------------------------------------------
Changes to 5.21

   - numerous cosmetic changes to graph and printing.

   - added graph hints for min and max temperatures and limits.

   - fixed bug with RI axis graduation size in printed graph.

   - fixed random backround colour for blood cooling point on graph.

   - showed point of blood cooling as white text, red background.

   - created new icons for application, data files.
    
   - prevented second instance of TRW running.

   - added drag and drop file opening.  Double-clicking a .TR file
     when Temprecord is already running will open the file in the open
     copy of Temprecord (previously an error was returned).      

   - fixed error when opening files with high temperatures and
     Refrigeration Index calculations enabled.
      
---------------------------------------------------------------------
Changes to 5.20

   - added MKT (Mean kinetic temperature) calculation 

   - added prompt to Auto Mode operation 

---------------------------------------------------------------------
Changes to 5.19

   - Help files converted to Compiled HTML format for better
     compatibility with Windows Vista.
     
   - PDF version of help supplied with installation.

   - web reuse notification function added   
   
   - colours of RI graph are now user-specified.

---------------------------------------------------------------------
Changes to 5.18

  - logger web registration and upload improved.

  - it is now possible to upload files without saving them to disk
    locally.

---------------------------------------------------------------------
Changes to 5.17

  - logger web registration and upload added.

  - display of web upload data and event log.

---------------------------------------------------------------------
Changes to 5.16

  - Auto mode added.
  
  - exiting Temprecord is now much faster.

  - open file and save file dialogs can now be resized.

---------------------------------------------------------------------
Changes to 5.15

  - fixed help entries on help menu.

  - fixed missing help buton on error dialogs.

  - added further explanation of Process Hygiene Index to help.

  - allowed a value of zero for the limit delay.

---------------------------------------------------------------------
Changes to 5.14

  - displayed graph temperature range increased to 255 degrees

---------------------------------------------------------------------
Changes to 5.13

  - units (C or F) now displayed in values screen.

  - the shift key is now no longer used when zooming in Graph view.

  - various minor coosmetic changes.

---------------------------------------------------------------------
Changes to 5.12

  - error in Rate-of-Cooling (ROC) calculations corrected (previously 
    ROC values were exactly twice what they should have been)

  - ROC values now displayed to 3 decimal places (previously 2)

  - unused ROC zones are now excluded when used zones are sorted
    into descending temperature order (previously all zones were
    sorted into descending temperature order)

---------------------------------------------------------------------
Changes to 5.10 and 5.11

  - internal changes necessitated by upgrade to new compiler (no 
    functional changes as seen by end user)

---------------------------------------------------------------------
Changes to 5.09

  - added support for TransLand logger.

  - fixed problems when loading filenames with special characters.

---------------------------------------------------------------------
Changes to 5.07

  - improvements made to USB driver installation.

---------------------------------------------------------------------
Changes to 5.06

  - fixed possible error when using drag-and-drop to start TRW.

  - added installation support for drivers for USB reader.

---------------------------------------------------------------------
Changes to 5.05

  - added support for auto-detection and assignment of USB-based COM 
    ports.  

---------------------------------------------------------------------
Changes to 5.04

  - added support for COM ports past COM16  

---------------------------------------------------------------------
Changes to 5.03
  
  - added support for Cryo sensors

---------------------------------------------------------------------
Changes to 5.02
  
  - fixed error in Rate of Cooling calculations.

  - when a password dialog opens, focus is placed in the input field.

---------------------------------------------------------------------
Changes to 5.01
  
  - when a logger is reused, the start delay is set to the current
    default start delay value, rather than being forced to 1 minute.

---------------------------------------------------------------------
Changes to 5.00
  
  - support for improved humidity sensors added.  Note that any 
    loggers reporting a firmware revision of 2.13 or later must use
    Temprecord 5.00 or later.

  - added support for blood cooling alerts.

---------------------------------------------------------------------
Changes to 4.09

  - preliminary support for improved Humidity sensors.

  - ESC now closes dialogs

  - Help button now works in dialogs

  - Start on Time and Date function disabled when setting up
    single use loggers.

---------------------------------------------------------------------
Changes to 4.06

  - fixed error when F7 used on graph view.

  - added 'Send' and Send All' menu entries to email files.

  - help button now functional when error message displayed after
    failure to communicate with logger.

  - attended to unfriendly font problem in some dialogs.

---------------------------------------------------------------------
Changes to 4.05

  - fix to date display when exporting data.

  - added "print all" function.

---------------------------------------------------------------------
Changes to 4.03

  - right-mouse click now no longer moves the graph cursor.

  - removed long delays when opening the Options tabs.

  - improved performance with network-based COM ports (such as 
    Moxa's NPort).

  - fixed problem where help file wasn't found.
 
---------------------------------------------------------------------
Changes to 4.02

  - export option added to produce TTV files suitable for MS Access.
 
  - Swedish language support added.

---------------------------------------------------------------------
Changes to 4.01

  - added dockable toolbars and main menu.

  - improved controls for selecting the start time and date.

  - the default filenames created when TRW asks to save or export a 
    file can be long filenames that include the time and date.  This 
    filename format is compatible with the Palm Temprecord software.

  - improved operation under Windows 2k family when using USB-to-
    serial adapters.

  - correct display and printing of date and time in Rate of Cooling 
    statistics when the user has selected a long date format in 
    their regional settings.

  - added option to produce a brief summary of the TTV statistics.

---------------------------------------------------------------------
Changes to 4.00

Version 4.00 is the first release of the 32-bit Windows version.  

---------------------------------------------------------------------
Reporting Problems

Problems when using TempRecord should be referred to your 
distributor.  When reporting problems, make sure you report
the version number of TempRecord you are running.  Click on
Help/About to see the version number. 

---------------------------------------------------------------------
