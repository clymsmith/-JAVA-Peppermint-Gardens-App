#!C:\xampp\perl\bin\perl.exe
use strict;
use warnings;
use CGI;
use JSON;

my $q = CGI->new;

print "Content-type: text/html\n\n";

if ($q->param("type") eq "test") {
    print "success";
} elsif ($q->param("type") eq "data") {

    # decode input string
    my $json_obj = decode_json($q->param("data"));
    # current time
    my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) = localtime();
    my $current_time = $hour . "_" . $min . "_" . $sec;

    my $filename = $json_obj->{"packets"}[0]->{"date"} . " " . $current_time . ".csv";
    my $final_output = "";

    # each input packet recieved. 
    # add to output json string
    foreach(@{ $json_obj->{"packets"} } ) {
        while((my $key, my $value) = each (%{$_})) {
            $final_output .= $key . "," . $value . "\n";
        }
        $final_output .= "\n";
    }

    # save csv file
    unless(open FILE, '>' . $filename) {
        die "data_file_fail";
    }
    print FILE $final_output;
    close FILE;
    print "success";

} else {
    print "fail";
}





