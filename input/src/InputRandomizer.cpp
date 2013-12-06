#include <iostream>
#include <fstream>
#include <cstdlib>
#include <ctime>

using namespace std;

int main(){

    unsigned int N;
    cout << "Enter number of co-ordinates to be generated" << endl;
    cin >> N;

    int format;
    cout << "Enter point format: 1 Integer 2: Float " << endl;
    cin >> format;

    char output[100];
    cout << "Enter output filename" << endl;
    cin >> output;

    int maxX, maxY;
    float fmaxX, fmaxY, fminX, fminY;

    if(format == 1){
        cout << "Enter Max range of X" << endl;
        cin >> maxX;

        cout << "Enter Max range of Y" << endl;
        cin >> maxY;
    } 

    if(format == 2){
        cout << "Enter Min range of X" << endl;
        cin >> fminX;

        cout << "Enter Min range of Y" << endl;
        cin >> fminY;

        cout << "Enter Max range of X" << endl;
        cin >> fmaxX;

        cout << "Enter Max range of Y" << endl;
        cin >> fmaxY;
    }

    ofstream ofile;
    ofile.open(output);

    // initialize random seed;
    srand(time(NULL));

    for(int i = 0; i < N; i++){
        if(format == 1){
            int x = rand() % maxX;
            int y = rand() % maxY;
            ofile << x << " " << y << endl;
        }
        if(format == 2){
            float x = (rand() / (static_cast<float>(RAND_MAX) + 1.0)) * (fmaxX-fminX) + fminX;
            float y = (rand() / (static_cast<float>(RAND_MAX) + 1.0)) * (fmaxY-fminY) + fminY;
            ofile << x << " " << y << endl;
        }
    }

    ofile.close();

    return 0;
}
