# Question 1:

## the Output will be :

### "Thread was interrupted!"
### "Thread will be finished here!!!"


the second sentence will be printed anyway because its in the finally block 

but becouse we write <thread.interrupt()> in the code the thread will interrupt and the InterruptedException will catch and first sentence will be printed

if we dont write <thread.interrupt()> it just wait for 10 seconds and then write <"Thread will be finished here!!!"> and terminate the program

<hr>

# Question 2:

## the Output will be :

### "Running in: main"

it means program will error free run but because we dont create any threads the name of currentThreat will be main (the master thread that all programs have it even single Thread programs like this question code)

<hr>

# Question 3:

## the Output will be :

### "Running in: Thread-0"
### "Back to: main"

when we <threadName.join()> in our code the main Thread of the program will wait for the Threat that we call join function of it to do all its task and when it finish the main Thread will run again

in this example the main Thread start at first when it reach to <thread.start()> the Thread-0 will started and main Thread will sleep until Thread-0 finish tasks and then it start again its
the reason of the order of sentences in the output