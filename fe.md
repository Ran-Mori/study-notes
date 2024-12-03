# FE

## four concepts

### webkit

* it is an **open-source web rendering engine** originally developed by **Apple**

### blink

#### feature

* it is still **open-source web rendering engine** developed by Google.
* it is a fork of webkit from 2013

#### why fork?

1.  Google wanted more control to make optimizations specific to Chrome.
2.  Google removed components (like WebKit2’s multi-process architecture) that were unnecessary for Chrome.
3.  The fork allowed Google to innovate without being constrained by WebKit’s development direction.

### Chromium

* it is a **open-source browser project** started by Google.

#### component

1. Blink
2. V8
3. Browser-specific components like networking, UI, and tab management.

### Chrome

* Google’s **proprietary web browser** based on the Chromium project.

#### feature

* it is **not open source**
* It adds proprietary components and features to Chromium, including: Google Sync, Google Brand, Usage Tracking.