#!/usr/local/bin/perl

### �ϐ��̒�`
my $infilename;
my $name;
my $ext;
my $size;
my $count=0;
my $data;
my @numnum;

### �t�@�C�����Ɗg���q�𕪗�
($name, $ext) = split(/\./, $ARGV[0]);

open FILE, "<./" . $ARGV[0] or die "open error:$!";
binmode(FILE);
$size = -s FILE;
read(FILE, $data, $size);
close FILE;

@numnum = split(/\,/, $data);

my %count;
@numnum = grep(!$count{$_}++, @numnum);

for(my $i=0; $i<@numnum; $i++){
	print $numnum[$i];
	print "\n";
}

exit 0;
__END__
