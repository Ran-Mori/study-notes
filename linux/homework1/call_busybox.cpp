#include<iostream>
#include<unistd.h>
#include<stdio.h>
#include<string.h>
#include<vector>
#include<stdlib.h>
#include<wait.h>
using namespace std;
vector<string> getCommand(){
	cout << ">";
	string input;
	getline(cin, input);
	vector<string> string_args;
	string arg = "";
	for (int i = 0; i < input.length(); i++)
	{
		switch (input[i])
		{
			case ' ': {
					  string_args.push_back(arg);
					  arg = "";
					  break;
				  }
			default: {
					 arg = arg + input[i];
					 break;
				 }
		}
	}
	string_args.push_back(arg);
	arg = "";
	return string_args;
}

void do_command(){
	int status;
	pid_t wpid;
	pid_t pid =fork();
	if(pid==0){
		char **args=(char**)malloc(sizeof(char *)*32);
		vector<string> _args=getCommand();
		size_t args_length=_args.capacity();
		for(size_t i=0;i<32;i++){
			if(i<args_length){
				char *_arg=new char[_args[i].length()+1];
				strcpy(_arg,_args[i].c_str());
				args[i]=_arg;
			}else{
				args[i]=nullptr;
			}
		}
		
		if(execvp("busybox",args)==-1){
			cout << "sorry your input is illegal,please enter ctrl C to restart";
		}
		cout << endl;
	}else if(pid>0){
		do {
			wpid = waitpid(pid, &status, WUNTRACED);
		} while (!WIFEXITED(status) && WIFSIGNALED(status));
	}
}
int main(){
	while(true){
		do_command();
	}
	return 0;
}

