if( @ARGV != 0 ){
	my $ARGV = shift( @ARGV );
	my $txt2_name = $ARGV . ".txt2";
	my $txt3_name = $ARGV . ".txt3";

	open( TXT2, "<$txt2_name" ) || die "File \"Not Open\" or \"Not File Name\"\n";
	open( TXT3, ">$txt3_name" );

	for(;;){
		if(0 == sysread(TXT2, $buffer, 4)){
			print "�I��\n";
			exit 0;
		}
		sysread(TXT2, $buffer, 4);
		syswrite(TXT3, $buffer, 4);
	}

	close( TXT2 );
	close( TXT3 );
}
else{
	print "�t�@�C�����i�g���q�����j���w�肵�Ă��������B\n";
}

