@echo off
:------------------------------------------------------------------------------
:    Batch file template for unattended installation of Temprecord software.
:    Copyright 2011 Temprecord International Ltd.
:
:    WARNING! ONLY SYSTEM ADMINISTRATORS SHOULD EDIT OR RUN THIS BATCH FILE
:    ======================================================================
:
:    This batch file installs Temprecord software ("TRW") with
:    no user interaction or prompts.  The installation folders and other
:    installation options are set by editing portions of this file.  Careless
:    editing may result in the overwriting or deletion of important files.
:
:    Please consult the Help once the software is installed.
:------------------------------------------------------------------------------
:
:   Edit the next 5 environment SET lines to suit your installation requirements
:
:   1. the folder where the application will be installed
:
    set InstallFolder=%ProgramFiles%\Temprecord\TRW Lab\
:
:
:   2. the folder where the installation log file will be written to
:
    set InstallLogFilename=%InstallFolder%TRW Lab Batch Installation Log.txt
:
:
:   3. the name of the program group created in the start menu
:
    set InstallGroup=Temprecord Lab
:
:
:   4. the type of installation - the options are:
:       JustMe                       (installs for the current user only)
:       AllUsers                     (installs for all users)
:       AllUsersShared               (installs for all users with shared settings)
:       AllUsersRemote               (installs for remote network users)
:
    set InstallType=AllUsers
:
:
:   5. whether existing settings are imported into the new installation
:      (NOTE - see the installation help) - the options are:
:       ImportSettings               (imports settings from existig INI file)
:       NoImportSettings             (does not import settings)
:
    set InstallImport=ImportSettings
:
:------------------------------------------------------------------------------
:
:   A note about INI files.  From TRW version 5.28 onwards, the INI file
:   handling is as follows:
:
:       JustMe installs:          INI file is in a LocalAppData folder
:       AllUsers installs:        INI file is in a LocalAppData folder
:       AllUsersShared installs:  INI file is a shared documents AppData folder
:       AllUsersRemote installs:  INI file is in the folder {TRW EXE folder}\Application Data\
:
:   Regardless of whether it is being run from this batch file or not, if the
:   installer does not find an existing INI file in the above location, and
:   the /NoImport setting is used, a default INI file from the folder the
:   setup EXE is run from is copied across .  If you don't want this, remove
:   any TRW.INI from the installation file set.
:
:   If you do require particular installation defaults and this is a "clean"
:   installation, with no possibility of an existing INI file begin already
:   in the installation INI folder, edit the file TRW.INI in the installation
:   file set before running the setup EXE.
:
:   If the installer finds an existing TRW.INI file and the /ImportSettings
:   option is specified, it is not replaced with the TRW.INI from the
:   installation file set, so existing settings should be preserved on
:   installation over previous versions.
:
:   IMPORTANT NOTE:  In the case of installation of the "Kiosk" version of the
:                    product, any existing INI file is ALWAYS replaced by the
:                    one supplied with the installer.
:
:------------------------------------------------------------------------------

    if zz%1 == zz/NoPrompt goto DoSetup
    echo You are about to execute an unattended batch install!  Press ^^C to abort, or
    pause

:DoSetup
    trw-setup.exe  /%InstallType% /%InstallImport% /SILENT /SP- /LOG="%InstallLogFilename%"  /SUPPRESSMSGBOXES  /DIR="%InstallFolder%" /GROUP="%InstallGroup%" /MERGETASKS="desktopicon,quicklaunchicon"

    set InstallLogFilename=
    set InstallFolder=
    set InstallGroup=
    set InstallType=
    set InstallImport=
    
