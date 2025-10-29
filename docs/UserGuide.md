---
layout: page
title: User Guide
---

HustleHub is a **desktop application** for computing students keeping track of multiple internship/job applications who prefer using CLI over GUI. The product will provide computing students an easy way to visualise, add and modify their job application details to multiple positions at multiple companies.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have Java `17` JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from this repository's [Releases](https://github.com/AY2526S1-CS2103T-T11-1/tp/releases) page.

1. Copy the file to the folder you want to use as the _home folder_ for HustleHub.

1. Open a command terminal, `cd` into the folder you put the jar file in, and run `java -jar <downloaded-jar-name>.jar` to start the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all applications.

   * `add n/Microsoft r/Cloud engineer s/INPROGRESS d/31 Oct` : Adds an application for `Microsoft` to HustleHub.

   * `delete 3` : Deletes the 3rd application shown in the current list.

   * `update 1 s/INPROGRESS d/2027-01-15T17:00` : Updates the 1st application's status and deadline.

   * `sort deadline` : Sorts the applications by **deadline** (ascending by default). You can also sort by `company` or `role`, and use `desc`. e.g. `sort company desc`

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Understanding HustleHub

Before diving into commands, let's understand the core concepts of HustleHub.

### Application Structure

Every job application in HustleHub contains the following information:

```
┌─────────────────────────────────────────────────────┐
│           Job Application Entry                     │
├─────────────────────────────────────────────────────┤
│ Company Name: Microsoft              [Required]     │
│ Role: Cloud Engineer                 [Required]     │
│ Status: INPROGRESS                   [Required]     │
│ Deadline: 31 Oct (23:59)             [Optional*]    │
│ Tags: remote, urgent, priority       [Optional]     │
│      (maximum 3 tags)                               │
└─────────────────────────────────────────────────────┘
```

**Key Points:**
- **Company**, **Role**, and **Status** are **required** when adding an application
- **Deadline** is **optional** - if omitted, defaults to today at 23:59
- **Company + Role** combination must be unique (no duplicates)
- **Tags** are optional and limited to 3 per application (max 30 characters each)
- **Deadline** must be a future date (supports multiple flexible formats)

### Status Lifecycle

Applications progress through three distinct stages:

```
   APPLIED ────────→ INPROGRESS ────────→ REJECTED
   (Submitted)       (Active Process)      (Closed)
      │                    │                   │
      │                    │                   │
   After you          During interviews,    After receiving
   click "Apply"      assessments, or       rejection
                      waiting for response
```

**Important Notes:**
- Status changes are **manual** - update them as your application progresses
- Use **tags** for more detailed tracking (e.g., `t/phone-screen`, `t/final-round`)
- Applications remain in your records even after rejection for future reference

### Filter vs. Find vs. Sort

Understanding the difference between these three operations:

```
┌─────────────────────────────────────────────────────────────┐
│ FILTER: Narrow down by exact property match                 │
│ ----------------------------------------------------------- │
│ [10 apps] ──filter s/APPLIED──> [3 apps with APPLIED]       │
│                                                             │
│ • Hides non-matching applications                           │
│ • Searches one field at a time (status, company, etc.)      │
│ • Exact match for status/deadline, partial for text         │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│ FIND: Search across company name AND role                   │
│ ----------------------------------------------------------- │
│ [10 apps] ──find Google engineer──> [4 matching apps]       │
│                                                             │
│ • Hides non-matching applications                           │
│ • Searches BOTH company name and role simultaneously        │
│ • Partial keyword matching (case-insensitive)               │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│ SORT: Reorder applications (no hiding)                      │
│ ----------------------------------------------------------- │
│ [A, Z, M, B] ──sort company──> [A, B, M, Z]                 │
│                                                             │
│ • Shows ALL applications, just reordered                    │
│ • Available fields: company, role, deadline                 │
│ • Ascending (asc) or descending (desc)                      │
└─────────────────────────────────────────────────────────────┘
```

**Quick Decision Guide:**

| I want to... | Use this command | Example |
|-------------|------------------|---------|
| See only applications with a specific status | `filter` | `filter s/APPLIED` |
| See only applications due on a date | `filter` | `filter d/2025-12-31` |
| Search for a company or role name | `find` | `find Google` or `find engineer` |
| Organize by deadline or alphabetically | `sort` | `sort deadline` or `sort company` |
| Reset and see everything | `filter none` or `list` | `filter none` or `list`|

--------------------------------------------------------------------------------------------------------------------

## Common Workflows

Now that you understand the core concepts, here are practical scenarios showing how to combine commands effectively.

### Scenario 1: Weekly Deadline Check
**Goal:** Review upcoming deadlines and prioritize urgent applications

1. `sort deadline` — See applications by earliest deadline
2. Review the top 5 results to identify time-sensitive applications
3. `tag 1 t/urgent` — Mark the most urgent application
4. `tag 2 t/urgent` — Mark the second urgent application
5. `filter t/urgent` — Focus view on only urgent applications

**Why this works:** Sorting by deadline surfaces what needs attention first, tagging creates a reusable category, and filtering lets you focus exclusively on what matters this week.

### Scenario 2: After Receiving Interview Invitation
**Goal:** Update application status and add preparation notes

1. `find Stripe` — Quickly locate the company (avoid scrolling through full list)
2. Note the index shown (e.g., application appears as #3)
3. `update 3 s/INPROGRESS` — Change status from APPLIED to INPROGRESS
4. `tag 3 t/system-design t/behavioral-prep` — Add interview preparation tags

**Why this works:** `find` is faster than manual searching, `update` changes the status in one command, and tags help you track what interview prep is needed.

### Scenario 3: End-of-Month Cleanup
**Goal:** Remove old rejected applications to keep your list focused

1. `filter s/REJECTED` — View only rejected applications
2. Review deadlines to identify outdated entries (e.g., rejections from 3+ months ago)
3. `delete 1` — Remove the first result
4. `delete 1` — Remove the next (which is now first after previous deletion)
5. `list` — Return to viewing all applications

**Why this works:** Filtering isolates what you want to clean up, repeated `delete 1` removes items efficiently as the list re-indexes, and `list` clears the filter to return to normal view.

### Scenario 4: Comparing Similar Roles
**Goal:** Find and compare backend engineer positions across companies

1. `find backend` — Search for applications with "backend" in company name or role
2. `sort deadline` — Order by application deadline (earliest first)
3. Review side-by-side: company names, deadlines, and existing tags
4. `tag 2 t/top-choice` — Mark your preferred option among the results
5. `tag 5 t/backup` — Tag a safe backup option

**Why this works:** `find` searches both company and role fields to locate backend positions, deadline sorting shows urgency, and tags help you categorize your preferences within the search results.

### Scenario 5: Preparing for Career Fair
**Goal:** Add multiple new applications quickly after a career fair

1. `add n/Meta r/Software Engineer s/APPLIED d/15 Nov` — Add first company (short date format!)
2. `add n/Netflix r/Backend Engineer s/APPLIED d/20 Nov t/career-fair` — Add second with tag
3. `add n/Salesforce r/Cloud Engineer s/APPLIED d/18 Nov t/career-fair` — Add third
4. `filter t/career-fair` — View all applications from this event
5. `sort deadline` — Prioritize by earliest deadline

**Why this works:** Using flexible date formats (like `15 Nov`) makes data entry much faster during busy career fairs. Batch adding with a consistent tag (`t/career-fair`) lets you track applications from the same source, filtering by that tag groups them together, and sorting helps you tackle applications strategically.

### Scenario 6: Following Up on Pending Applications
**Goal:** Identify applications with no response for 2+ weeks

1. `filter s/APPLIED` — Show applications still in "applied" status
2. `sort deadline asc` — Order by deadline (earliest first)
3. Review companies where deadline is approaching but status hasn't changed
4. `tag 3 t/follow-up` — Mark applications that need a status check email
5. `filter t/follow-up` — Focus on applications requiring action

**Why this works:** Status filtering finds stagnant applications, deadline sorting reveals time pressure, tags create an actionable todo list, and re-filtering keeps you focused on follow-up tasks.

--------------------------------------------------------------------------------------------------------------------

## Command summary

### General Commands

| Command                                      | Description                                  | Format |
|----------------------------------------------|----------------------------------------------|--------|
| [**exit**](#exiting-the-program--exit)       | Exits the program                            | `exit` |
| [**help**](#viewing-help--help)              | Displays how to use all commands in a window | `help` |
| [**list**](#listing-all-applications--list)  | Lists all applications                       | `list` |

### Application Management

| Command                                          | Description                                      | Format                                                                     |
|--------------------------------------------------|--------------------------------------------------|----------------------------------------------------------------------------|
| [**add**](#adding-a-job-application-add)         | Adds a job application to HustleHub              | `add n/COMPANY_NAME r/ROLE s/STATUS [d/DEADLINE] [t/TAG]…​`                |
| [**delete**](#deleting-an-application--delete)   | Deletes a job application given its index number | `delete INDEX`                                                             |
| [**find**](#finding-job-applications-find)       | Finds job applications by company name or role   | `find KEYWORD [MORE_KEYWORDS]`                                             |
| [**filter**](#filtering-job-applications-filter) | Filters job applications by a property           | `filter FLAG/KEYWORD`                                                      |
| [**sort**](#sorting-the-applications--sort)      | Sorts the job applications in HustleHub          | `sort FIELD`                                                               |
| [**update**](#updating-a-job-application-update) | Updates an existing job application's details    | `update INDEX [n/COMPANY_NAME] [r/ROLE] [s/STATUS] [d/DEADLINE] [t/TAG]…​` |

### Tag Management

| Command                           | Description                                   | Format                                  |
|-----------------------------------|-----------------------------------------------|-----------------------------------------|
| [**tag**](#adding-tags-tag)       | Adds new tags to a specified job application  | `tag JOB_APPLICATION_INDEX t/NEW_TAG`   |
| [**untag**](#removing-tags-untag) | Removes tags from a specified job application | `untag JOB_APPLICATION_INDEX t/NEW_TAG` |

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/COMPANY_NAME`, `COMPANY_NAME` is a parameter which can be used as `add n/Google r/Software Engineer s/APPLIED d/2025-12-31T23:59`.

* Items in square brackets are optional.<br>
  e.g. `n/COMPANY_NAME [t/TAG]` can be used as `n/Google t/priority` or as `n/Google`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/urgent`, `t/urgent t/remote` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/COMPANY_NAME r/ROLE`, `r/ROLE n/COMPANY_NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.

**HustleHub uses three fixed status values to track your progress**

| Status | Meaning | When to use |
|--------|---------|-------------|
| `APPLIED` | Application submitted | After you click "Apply" |
| `INPROGRESS` | Active process | During interviews, assessments, or waiting for response |
| `REJECTED` | Closed unsuccessfully | After receiving rejection |
*Important notes:*
- Status values are **case-insensitive** in commands: `applied` = `APPLIED`
- These are the ONLY valid values; custom statuses are not supported
- Use tags for more granular tracking (e.g., `t/phone-screen`, `t/final-round`)

**Date & Time Formats in HustleHub**

HustleHub now supports **flexible date formats** to make adding applications faster and easier!

*When adding/updating applications, you can use any of these formats:*

**Full Date-Time Formats** (if you need a specific time):
- `yyyy-MM-ddTHH:mm` → `2025-12-31T23:59`
- `yyyy-MM-dd HH:mm` → `2025-12-31 23:59`

**Date-Only Formats** (defaults to 23:59):
- `yyyy-MM-dd` → `2025-12-31` (defaults to 23:59)
- `MM-dd` → `12-31` (infers current or next year, defaults to 23:59)
- `dd MMM` → `31 Dec` (infers year, defaults to 23:59)
- `dd MMMM` → `31 December` (infers year, defaults to 23:59)
- `dd-MMM` → `31-Dec` (infers year, defaults to 23:59)

**No Deadline Specified**:
- Simply omit the `d/` flag entirely, and the deadline defaults to **today at 23:59**

*When filtering by deadline:*
- Format: `yyyy-MM-dd` (time not needed)
- Example: `2025-12-31`
- Matches all applications due on that date regardless of time

*Smart Year Inference:*
- When you use formats like `12-31` or `31 Dec` without specifying the year:
  - If the date would be in the past, HustleHub automatically uses **next year**
  - Otherwise, it uses the **current year**

*Rules:*
- Must be a future date (no past deadlines)
- Time component uses 24-hour format when specified
</div>

--------------------------------------------------------------------------------------------------------------------

## General Commands

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Viewing help : `help`

Shows a message explaining how to access the help page.

Format: `help`

Result for `help`:
![help message](images/helpMessage.png)

### Listing all applications : `list`

Lists all job applications in HustleHub. This is useful after filtering to return to viewing all applications.

Format: `list`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
`list` and `filter none` have the same effect - both show all applications.
</div>

### Saving data

HustleHub data is automatically saved after any command that changes the data. There is no need to save manually.

### Editing the data file

HustleHub data is saved automatically as a JSON file `[JAR file location]/data/JobApplications.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file make its format invalid, HustleHub will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause HustleHub to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

---

## Application Management

### Adding a job application: `add`

Adds a job application to HustleHub.

Format: `add n/COMPANY_NAME r/ROLE s/STATUS [d/DEADLINE] [t/TAG]…​`

<div markdown="span" class="alert alert-primary">:bulb: **Tips:**

* The deadline (`d/`) is **optional**. If omitted, it defaults to today at 23:59.
* You can use **flexible date formats** for the deadline - see the Date & Time Formats section above for all supported formats.
* An application can have up to 3 tags.
* New applications appear at the **top** of your list for easy visibility.
</div>

Examples:
* `add n/Google r/Software Engineer s/APPLIED` - No deadline specified, defaults to today
* `add n/Microsoft r/Cloud Engineer s/INPROGRESS d/31 Oct` - Short date format
* `add n/Amazon r/Data Scientist s/APPLIED d/12-25` - Month-day format
* `add n/Meta r/Frontend Developer s/APPLIED d/2025-11-30T14:00` - Full date-time format
* `add n/Netflix r/Backend Engineer s/APPLIED d/15 December t/remote t/urgent` - With tags

### Deleting an application : `delete`

Deletes the specified application from HustleHub.

Format: `delete INDEX`

* Deletes the application at the specified `INDEX`.
* The index refers to the index number shown in the displayed application list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd application in HustleHub.
* After filtering the list, `delete 1` deletes the 1st application in the currently displayed results.

### Finding job applications: `find`

Finds job applications whose **Company Name** or **Role** matches any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

#### **Find Rules**

1. **Search Scope**: Searches both **Company Name** and **Role** fields.
   * An application is returned if either field contains at least one keyword.

2. **Case-Insensitive**: The search is case-insensitive.
   * `tiktok` matches "TikTok", "TikTok Inc", or "BigTikTok".

3. **Partial Matching**: Keywords match partial words.
   * `engineer` matches "Software Engineer", "Engineering Manager".

4. **Keyword Order**: The order of keywords does not matter.
   * `American Airlines` matches "Airlines American".

5. **OR Search**: Applications matching **at least one keyword** in **either field** are returned.
   * `find Morgan engineer` returns applications where:
     * Company contains "Morgan" (e.g., "J.P. Morgan"), OR
     * Role contains "engineer" (e.g., "Software Engineer"), OR
     * Both

#### **Examples**

* `find morgan`
  * Returns applications with company "Morgan Stanley" or "J.P. Morgan Chase".

* `find engineer`
  * Returns applications with roles like "Software Engineer", "Data Engineer", "Engineering Intern".

* `find Tiger America`
  * Returns "Tiger Management" (company), "Bank of America" (company), or any role containing "Tiger" or "America".

* `find backend google`
  * Returns applications where:
    * Company contains "Google", OR
    * Role contains "backend" (e.g., "Backend Developer"), OR
    * Both

Before: `find morgan engineer`

![find command before](images/find-command-before.png)

After: Returns all applications where company contains "morgan" OR role contains "engineer"

![find command](images/find-command.png)

### Filtering Job Applications: `filter`

Filters the list of job applications based on a single field: **Tags**, **Status**, or **Application Deadline**.
* Filter command accepts only one filter flag at a time.

Format: `filter FLAG/KEYWORD`

or, to remove all filters: `filter none`


#### **Filtering Rules**


1. **Tags (`t/`)**: Matches if the `KEYWORD` is **contained** in any tag
   (e.g., `t/backend` matches a tag named "backend-engineer").
    * The search is **case-insensitive**.


2. **Application Status (`s/`)**: Matches an exact, **case-insensitive** status.
    * Valid keywords are: **APPLIED**, **INPROGRESS**, or **REJECTED**.


3. **Application Deadline (`d/`)**: Matches the exact date only, ignoring the time component.
    * The date must be in the **`yyyy-MM-dd`** format (e.g., `2025-12-31`).
    * Matches all applications due on that date regardless of time

#### **Examples**

* `filter t/backend`
    * Returns applications with tags containing "backend" (e.g., "backend", "backend-dev").


* `filter s/applied`
    * Returns applications with the status "APPLIED".


* `filter d/2025-10-20`
    * Returns applications with an application deadline on October 20, 2025.


* `filter none`
    * Removes all current filters and shows the complete list of job applications.

Before: `filter s/inprogress`

![result for 'filter s/inprogress'](images/filterInProgressCommand.png)
      
After filtering: `INPROGRESS`

![result for 'filter s/inprogress'](images/filterInProgressResult.png)

### Sorting the applications : `sort`

Sorts the current list of applications by a chosen field, in ascending or descending order.

**Format:** `sort FIELD [ORDER]`
- **FIELD**: `company` \| `role` \| `deadline`
- **ORDER** (optional): `asc` \| `ascending` \| `desc` \| `descending` (default: `asc`)

**Notes:**
- Sorting is **stable** and **case-insensitive** for text fields (`company`, `role`).
- When sorting by **deadline**, missing/invalid deadlines appear **last** for `asc` (and **first** for `desc`).

**Examples:**
- `sort deadline`
- `sort company desc`
- `sort role ascending`

Before:
![sort_before.png](images/sort_before.png)

After:
![sort_after.png](images/sort_after.png)

### Updating a job application: `update`

Updates the details of an existing job application in HustleHub.

Format: `update INDEX [n/COMPANY_NAME] [r/ROLE] [s/STATUS] [d/DEADLINE] [t/TAG]…​`

* Updates the job application at the specified `INDEX`.
* The index refers to the index number shown in the displayed application list.
* The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be overwritten by the input values.
* When updating tags with `t/TAG`, all existing tags will be replaced by the new tags specified.
* You can remove all tags by typing `t/` without specifying any tags after it.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**

* Deadline cannot be in the past. Please provide a future date (supports flexible formats - see Date & Time Formats section).
* Updating company name and/or role may result in duplicate applications if the combination already exists.
* Valid status values are: `APPLIED`, `INPROGRESS`, or `REJECTED` (case-insensitive).

</div>

**Examples:**

* `update 1 s/INPROGRESS` - Updates the status of the 1st application to INPROGRESS.
* `update 2 r/Senior Engineer d/15 Jan` - Updates the role and deadline using short date format.
* `update 3 n/Apple r/iOS Developer s/APPLIED d/30 June t/remote t/urgent` - Updates all fields with flexible date.
* `update 4 d/2027-01-15T14:00` - Updates deadline with specific time.
* `update 1 t/` - Removes all tags from the 1st application.

Before:

![update_before.png](images/update_before.png)

After updating with `update 2 s/INPROGRESS d/2027-01-15T17:00 t/priority t/urgent`:

![update_after.png](images/update_after.png)

--------------------------------------------------------------------------------------------------------------------

## Tag Management

### Adding tags: `tag`

Adds tags to a job application.

Format: `tag JOB_APPLICATION_INDEX t/TAG...`

Input restrictions:
1. A `TAG` must be 1 to 30 characters long (cannot be blank).
2. A `TAG` is a single word (no spaces).
3. Each job application can have up to 3 tags.
4. Allowed characters: letters, digits, and at most 2 of the following special characters: `-`, `.`, `@`, `#`, `_`, `+`.

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
If you add a mix of duplicate and new tags, the new tags will be added whilst duplicates will be ignored.
</div>

**Example Tagging Strategies:**

**By priority:**
- `tag 1 t/first-choice t/dream-job`
- `tag 5 t/backup t/safe-option`

**By work arrangement:**
- `tag 2 t/remote t/flexible-hours`
- `tag 7 t/onsite t/relocation-required`

**By preparation needs:**
- `tag 3 t/leetcode-heavy t/system-design`
- `tag 4 t/C# t/Full-stack`

**By timeline:**
- `tag 1 t/fast-response t/rolling`
- `tag 6 t/late-deadline t/backup-plan`

<div style="display: flex; gap: 20px;">
  <div style="flex: 1;">
    <strong>Before:</strong><br>
    <img src="images/tag_before.png" alt="tag_before.png">
  </div>
  <div style="flex: 1;">
    <strong>After tagging:</strong><br>
    <img src="images/tag_after.png" alt="tag_after.png">
  </div>
</div>

### Removing tags: `untag`

Removes tags from a job application.

Format: `untag JOB_APPLICATION_INDEX t/TAG...`

Input restrictions:
1. A `TAG` must be 1 to 30 characters long (cannot be blank).
2. A `TAG` is a single word (no spaces).
3. Allowed characters: letters, digits, and at most 2 of the following special characters: `-`, `.`, `@`, `#`, `_`, `+`.
4. Inputted tags must already exist on the application.

Examples:
- `untag 1 t/SQL`
- `untag 2 t/6-Month t/C++`
- `untag 3 t/python_v3.12 t/BlockChain t/Full-Stack`

Before:

![Untag_before.png](images/Untag_before.png)

After untagging:

![Untag_after.png](images/Untag_after.png)

---

## FAQ

**Q**: How do I transfer my data to another computer?<br>
**A**: Install the app on the other computer and overwrite the empty data file it creates with the file that contains the data of your previous HustleHub home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.
