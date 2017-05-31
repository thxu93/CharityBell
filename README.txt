README
CHARITY BELL
TCSS450
Adam Waldron
Thomas Xu

Charity Bell

This application was developed to help curb the bad habit of over snoozing an alarm, while
simultaneously donating money to a good cause. To use the application you must first register
with a valid email and password, then once logged in, you can set up to 100 alarms which will cause
the device to vibrate and make an alarm sound at the chosen time. You can then either cancel the
alarm, or snooze. If you snooze a dollar is added to the donation amount. Upon cancel, you are
prompted to share whether you snoozed or not to social media to promote accountability.

Features Implemented:

1. We fixed the ambiguity noted from our previous phases’ peer review where the user was confused as
to whether the alarm was set or not.
2. We were not able to implement the use case for “yelling” at the alarm to cancel it. We were also
not able to fully implement the Google Play integration for payment as it required a paid
subscription for the Google Play Developer Console and the Donation API is not yet released.
https://developers.google.com/donation/
3. We save the number of times the user snoozes the alarm using SharedPreferences and use that count
to customize the social media dialog.
4. We utilized the firebase DB for our web services, saving the Alarm to the DB when it is set and
removing it when it is deleted. Also, when the user logs in, all alarms retrieved from the DB will
be automatically set to the device.
5. We implemented Facebook content sharing, when a user cancels the alarm, it automatically
generates a post to Facebook dialog.
6. Our application implements custom sign in using Firebase authentication with a valid email and
password over 5 characters.
7. We implemented a custom Icon for our graphics in this phase.
8. We wrote JUnit tests for the Alarm class. We also wrote instrumentation tests for the
RegisterActivity.

User Stories From Proposal:

1. As a user I want to set an alarm so that I can be woken up by a dialog and audible alarm at a
certain time. Fully implemented, we also added device vibration.
2. As a user I want to be able to snooze the alarm so that I can sleep longer and donate to charity.
Fully implemented, we also added a counter that will track the number of times snoozed.
3. As a user I want to be able to set a flexible alarm so I can set either one alarm or multiple
alarms. Fully implemented, we also added the ability to delete alarms and display all the alarms in
a recycler view which remain persistent and synced to the Firebase DB.
4. As a user I want to be able to automatically post my snoozes and donation amounts to social media
so I can promote personal accountability. Fully implemented.