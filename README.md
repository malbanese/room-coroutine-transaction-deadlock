# room-coroutine-transaction-deadlock
Example repository to reproduce a coroutine + transaction deadlock bug in Room

There appears to be a bug within abstract suspending database transaction functions. If the coroutine
is cancelled while the transaction is still performing operations, there is a chance that the global Room transaction
lock never gets unlocked.
